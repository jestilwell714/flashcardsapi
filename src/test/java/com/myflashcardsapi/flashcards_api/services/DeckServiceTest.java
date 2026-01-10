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

import java.util.List;
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
    private Folder folder3;
    private FlashCard flashCard;
    private DeckDto deckDto;
    private DeckDto deckDto2;
    private DeckDto deckDto3;
    private Deck deck;
    private Deck deck2;
    private Deck deck3;
    private Tag tag;


    @BeforeEach
    void setUp() {
        testEntityBuilder = new TestEntityBuilderUnit();
        testEntityBuilder.testEntitySetUp();
        user = testEntityBuilder.getUser();
        folder = testEntityBuilder.getCosc201Folder();
        folder2 = testEntityBuilder.getCosc204Folder();
        folder3 = testEntityBuilder.getRootFolder();
        flashCard = testEntityBuilder.getFlashCard1();

        deck = testEntityBuilder.getDeck1();
        deck2 = testEntityBuilder.getDeck2();
        deck3 = testEntityBuilder.getDeck3();
        tag = testEntityBuilder.getDataStructureTag();

        deckDto = new DeckDto();
        deckDto.setId(deck.getId());
        deckDto.setName(deck.getName());
        deckDto.setFolderId(folder.getId());
        deckDto2 = new DeckDto();
        deckDto2.setId(deck2.getId());
        deckDto2.setName(deck2.getName());
        deckDto2.setFolderId(folder2.getId());
        deckDto3 = new DeckDto();
        deckDto3.setId(deck3.getId());
        deckDto3.setName(deck3.getName());
        deckDto3.setFolderId(null);
    }

    @Test
    void givenDtoWhenCreateDeckReturnDto() throws BadRequestException {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockFolderRepository.findByIdAndUserId(deckDto.getFolderId(), user.getId())).thenReturn(Optional.of(folder));
        when(mockDeckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), user.getId(), folder.getId())).thenReturn(false);
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
        when(mockFolderRepository.findByIdAndUserId(deckDto.getFolderId(), user.getId())).thenReturn(Optional.of(folder));
        when(mockDeckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), user.getId(), folder.getId())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> deckService.createDeck(user.getId(), deckDto));
    }

    @Test
    void givenDtoWhenMovedFolderThenReturnUpdateDto() throws BadRequestException {
        deckDto.setName("Lecture 2");
        deckDto.setFolderId(folder2.getId());
        when(mockDeckRepository.findByIdAndUserId(deck.getId(), user.getId())).thenReturn(Optional.of(deck));
        when(mockDeckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), user.getId(), deckDto.getFolderId())).thenReturn(false);
        when(mockFolderRepository.findByIdAndUserId(deckDto.getFolderId(), user.getId())).thenReturn(Optional.of(folder2));
        when(mockDeckRepository.save(deck)).thenReturn(deck);
        when(mockDeckMapper.mapTo(deck)).thenReturn(deckDto);

        DeckDto updatedDeckDto = deckService.updateDeck(user.getId(), deck.getId(), deckDto);
        assertThat(updatedDeckDto).isNotNull();
        assertThat(updatedDeckDto.getName()).isEqualTo(deckDto.getName());
        assertThat(updatedDeckDto.getFolderId()).isEqualTo(deckDto.getFolderId());
        verify(mockDeckRepository).save(deck);
    }

    @Test
    void givenDtoWhenUpdatedNameThenReturnUpdateDto() throws BadRequestException {
        deckDto.setName("Lecture 2");
        when(mockDeckRepository.findByIdAndUserId(deck.getId(), user.getId())).thenReturn(Optional.of(deck));
        when(mockDeckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), user.getId(), deckDto.getFolderId())).thenReturn(false);
        when(mockDeckRepository.save(deck)).thenReturn(deck);
        when(mockDeckMapper.mapTo(deck)).thenReturn(deckDto);

        DeckDto updatedDeckDto = deckService.updateDeck(user.getId(), deck.getId(), deckDto);
        assertThat(updatedDeckDto).isNotNull();
        assertThat(updatedDeckDto.getName()).isEqualTo(deckDto.getName());
        assertThat(updatedDeckDto.getFolderId()).isEqualTo(deckDto.getFolderId());
        verify(mockDeckRepository).save(deck);
    }

    @Test
    void givenDtoWhenMovedToNoParentFolderThenReturnUpdateDto() throws BadRequestException {
        deckDto.setFolderId(null);
        when(mockDeckRepository.findByIdAndUserId(deck.getId(), user.getId())).thenReturn(Optional.of(deck));
        when(mockDeckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), user.getId(), deckDto.getFolderId())).thenReturn(false);
        when(mockDeckRepository.save(deck)).thenReturn(deck);
        when(mockDeckMapper.mapTo(deck)).thenReturn(deckDto);

        DeckDto updatedDeckDto = deckService.updateDeck(user.getId(), deck.getId(), deckDto);
        assertThat(updatedDeckDto).isNotNull();
        assertThat(updatedDeckDto.getName()).isEqualTo(deckDto.getName());
        assertThat(updatedDeckDto.getFolderId()).isEqualTo(deckDto.getFolderId());
        verify(mockDeckRepository).save(deck);
    }

    @Test
    void givenDtoWhenMovedFolderWithDeckWithSameNameThenThrowException() throws BadRequestException {
        deckDto.setFolderId(folder2.getId());
        when(mockDeckRepository.findByIdAndUserId(deck.getId(), user.getId())).thenReturn(Optional.of(deck));
        when(mockDeckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), user.getId(), deckDto.getFolderId())).thenReturn(true);


        assertThrows(BadRequestException.class, () -> deckService.updateDeck(user.getId(), deck.getId(), deckDto));
    }

    @Test
    void givenUserIdWhenGetAllDeckDtosForUserReturnListOfDeck1Deck2AndDeck3() {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockDeckRepository.findByUserId(user.getId())).thenReturn(List.of(deck, deck2, deck3));
        when(mockDeckMapper.mapTo(deck)).thenReturn(deckDto);
        when(mockDeckMapper.mapTo(deck2)).thenReturn(deckDto2);
        when(mockDeckMapper.mapTo(deck3)).thenReturn(deckDto3);
        List<DeckDto> deckDtoList = deckService.getAllDeckDtosForUser(user.getId());
        assert(deckDtoList).containsAll(List.of(deckDto, deckDto2, deckDto3));
    }

    @Test
    void givenFolderIdAndUserIdWhenGetAllDeckDtosForFolderReturnListOfDeck1() {
        when(mockFolderRepository.findByIdAndUserId(folder.getId(),user.getId())).thenReturn(Optional.of(folder));
        when(mockDeckRepository.findByFolderIdAndUserId(folder.getId(), user.getId())).thenReturn(List.of(deck));
        when(mockDeckMapper.mapTo(deck)).thenReturn(deckDto);
        List<DeckDto> deckDtoList = deckService.getAllDeckDtosByFolderIdAndUser(folder.getId(),user.getId());
        assert(deckDtoList.contains(deckDto));
    }


    @Test
    void givenFolderIdAndUserIdWhenGetAllDeckDtosForFolderIdIsNUllReturnListOfDeck1() {
        when(mockDeckRepository.findByFolderIdAndUserId(null, user.getId())).thenReturn(List.of(deck3));
        when(mockDeckMapper.mapTo(deck3)).thenReturn(deckDto3);
        List<DeckDto> deckDtoList = deckService.getAllDeckDtosByFolderIdAndUser(null,user.getId());
        assert(deckDtoList.contains(deckDto3));
    }
}
