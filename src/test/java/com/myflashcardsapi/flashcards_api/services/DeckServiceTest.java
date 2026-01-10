package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.*;
import com.myflashcardsapi.flashcards_api.domain.dto.DeckDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.DeckMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.DeckRepository;
import com.myflashcardsapi.flashcards_api.repositories.FolderRepository;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.impl.DeckServiceImpl;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilderUnit;
import org.apache.coyote.BadRequestException;
import org.hibernate.jdbc.BatchFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeckServiceTest {
    @Mock
    private DeckRepository mockDeckRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private FolderRepository mockFolderRepository;

    @Mock
    private DeckMapperImpl mockDeckMapper;

    @InjectMocks
    private DeckServiceImpl deckService;

    private TestEntityBuilderUnit testEntityBuilder;

    private User user;
    private Folder folder;
    private Folder folder2;
    private FlashCard flashCard;
    private DeckDto deckDto;
    private Deck deck;
    private Tag tag;


    @BeforeEach
    void setUp() {
        testEntityBuilder = new TestEntityBuilderUnit();
        testEntityBuilder.testEntitySetUp();
        user = testEntityBuilder.getUser();
        folder = testEntityBuilder.getCosc201Folder();
        folder2 = testEntityBuilder.getRootFolder();

        flashCard = testEntityBuilder.getFlashCard1();

        deck = testEntityBuilder.getDeck1();
        tag = testEntityBuilder.getDataStructureTag();

        deckDto = new DeckDto();
        deckDto.setId(deck.getId());
        deckDto.setName(deck.getName());
        deckDto.setFolderId(folder.getId());
    }

    @Test
    void givenDtoWhenCreateDeckReturnDto() throws BadRequestException {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockFolderRepository.findByIdAndUserId(deckDto.getFolderId(),user.getId())).thenReturn(Optional.of(folder));
        when(mockDeckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(),user.getId(),folder.getId())).thenReturn(false);
        when(mockDeckMapper.mapFrom(deckDto)).thenReturn(deck);
        when(mockDeckRepository.save(deck)).thenReturn(deck);
        when(mockDeckMapper.mapTo((deck))).thenReturn(deckDto);

        DeckDto createdDeckDto = deckService.createDeck(user.getId(), deckDto);

        assertThat(createdDeckDto).isNotNull();
        assertThat(createdDeckDto.getName()).isEqualTo(deckDto.getName());
        verify(mockDeckRepository).save(deck);
    }

    @Test
    void givenDuplicateNameWhenCreateDeckThenThrowsException() {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockFolderRepository.findByIdAndUserId(deckDto.getFolderId(),user.getId())).thenReturn(Optional.of(folder));
        when(mockDeckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(),user.getId(),folder.getId())).thenReturn(true);

        assertThrows(BadRequestException.class,() -> deckService.createDeck(user.getId(), deckDto));
    }

    @Test
    void givenDtoWhenMovedFolderThenReturnUpdateDto() throws BadRequestException {
        deckDto.setName("Lecture 2");
        deckDto.setFolderId(folder2.getId());
        when(mockDeckRepository.findByIdAndUserId(deck.getId(), user.getId())).thenReturn(Optional.of(deck));
        when(mockFolderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(deckDto.getName(), deckDto.getFolderId(),user.getId())).thenReturn(false);
        when(mockFolderRepository.findByIdAndUserId(deckDto.getFolderId(),user.getId())).thenReturn(Optional.of(folder2));
        when(mockDeckRepository.save(deck)).thenReturn(deck);
        when(mockDeckMapper.mapTo(deck)).thenReturn(deckDto);

        DeckDto updatedDeckDto = deckService.updateDeck(user.getId(),deck.getId(),deckDto);
        assertThat(updatedDeckDto).isNotNull();
        assertThat(updatedDeckDto.getName()).isEqualTo(deckDto.getName());
        assertThat(updatedDeckDto.getFolderId()).isEqualTo(deckDto.getFolderId());
        verify(mockDeckRepository).save(deck);
    }

    @Test
    void givenDtoWhenMovedFolderWithDeckWIthNameThenThrowException() throws BadRequestException {
        deckDto.setFolderId(folder2.getId());
        when(mockDeckRepository.findByIdAndUserId(deck.getId(), user.getId())).thenReturn(Optional.of(deck));
        when(mockFolderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(deckDto.getName(), deckDto.getFolderId(),user.getId())).thenReturn(false);
        when(mockFolderRepository.findByIdAndUserId(deckDto.getFolderId(),user.getId())).thenReturn(Optional.of(folder2));
        when(mockDeckRepository.save(deck)).thenReturn(deck);
        when(mockDeckMapper.mapTo(deck)).thenReturn(deckDto);

        DeckDto updatedDeckDto = deckService.updateDeck(user.getId(),deck.getId(),deckDto);
        assertThrows(BadRequestException.class,() -> deckService.createDeck(user.getId(), deckDto));
    }

}
