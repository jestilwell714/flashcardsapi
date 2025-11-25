package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.Tag;
import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.FlashCardMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.FlashCardRepository;
import com.myflashcardsapi.flashcards_api.repositories.TagRepository;
import com.myflashcardsapi.flashcards_api.services.impl.FlashCardServiceImpl;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilderUnit;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlashCardServiceTest {

    @Mock
    private FlashCardRepository mockFlashcardRepository;

    @Mock
    private DeckService mockDeckService;

    @Mock
    private FolderService mockFolderService;

    @Mock
    private TagRepository mockTagRepository;

    @Mock
    private FlashCardMapperImpl mockFlashcardMapper;

    @InjectMocks
    private FlashCardServiceImpl flashcardService;

    private TestEntityBuilderUnit testEntityBuilder;

    private User user;
    private FlashCardDto flashCardDto;
    private FlashCard flashCard;
    private Deck deck;
    private Deck deck2;
    private Tag tag;
    private Tag tag2;

    @BeforeEach
    void setUp() {

        testEntityBuilder = new TestEntityBuilderUnit();
        testEntityBuilder.testEntitySetUp();
        user = testEntityBuilder.getUser();

        flashCard = testEntityBuilder.getFlashCard1();

        deck = testEntityBuilder.getDeck1();

        deck2 = testEntityBuilder.getDeck2();

        tag = testEntityBuilder.getDataStructureTag();

        tag2 = testEntityBuilder.getAlgorithmsTag();


        flashCardDto = new FlashCardDto();
        flashCardDto.setId(flashCard.getId());
        flashCardDto.setAnswer("A linear data structure that follows the LIFO (Last-In, First-Out) principle. Operations include Push (add) and Pop (remove).");
        flashCardDto.setQuestion("What is a stack?");
        flashCardDto.setDeckId(deck.getId());
        flashCardDto.setTagIds(List.of(tag.getId()));
    }

    @Test
    void givenDtoWhenCreateFlashcardThenReturnDto() throws BadRequestException {
        when(mockFlashcardMapper.mapTo(flashCard)).thenReturn(flashCardDto);
        when(mockDeckService.getDeckByIdAndUser(deck.getId(),user.getId())).thenReturn(Optional.ofNullable(deck));
        when(mockTagRepository.findByIdInAndUserId(flashCardDto.getTagIds(), user.getId())).thenReturn(List.of(tag));
        when(mockFlashcardRepository.save(flashCard)).thenReturn(flashCard);
        when(mockFlashcardMapper.mapFrom(flashCardDto)).thenReturn(flashCard);

        FlashCardDto createdFlashCardDto = flashcardService.createFlashCard(user.getId(),deck.getId(),flashCardDto);

        assertThat(createdFlashCardDto).isNotNull();
        assertThat(createdFlashCardDto.getQuestion()).isEqualTo(flashCardDto.getQuestion());
        assertThat(createdFlashCardDto.getAnswer()).isEqualTo(flashCardDto.getAnswer());
        assertThat(createdFlashCardDto.getDeckId()).isEqualTo(flashCardDto.getDeckId());
        assertThat(createdFlashCardDto.getTagIds()).containsExactlyInAnyOrder(tag.getId());
    }

    @Test
    void givenInvalidDeckIdWhenCreateFlashcardThenThrowsException() {
        when(mockDeckService.getDeckByIdAndUser(deck.getId(),user.getId())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> flashcardService.createFlashCard(user.getId(), deck.getId(), flashCardDto));
    }

    @Test
    void givenDtoWhenUpdateFlashcardThenReturnUpdateDto() throws BadRequestException {
        flashCardDto.setQuestion("What is the primary goal of a sorting algorithm?");
        flashCardDto.setAnswer("To arrange elements of a list in a specific order");
        flashCardDto.setDeckId(deck2.getId());
        flashCardDto.setTagIds(List.of(tag2.getId()));

        when(mockFlashcardRepository.findByIdAndDeckUserId(flashCard.getId(), deck.getId())).thenReturn(Optional.ofNullable(flashCard));
        when(mockFlashcardRepository.save(flashCard)).thenReturn(flashCard);
        when(mockTagRepository.findByIdInAndUserId(flashCardDto.getTagIds(), user.getId())).thenReturn(List.of(tag2));
        when(mockDeckService.getDeckByIdAndUser(deck2.getId(),user.getId())).thenReturn(Optional.ofNullable(deck2));
        when(mockFlashcardRepository.save(flashCard)).thenReturn(flashCard);
        when(mockFlashcardMapper.mapTo(flashCard)).thenReturn(flashCardDto);

        FlashCardDto updatedFlashCardDto = flashcardService.updateFlashCard(user.getId(),flashCard.getId(),flashCardDto);

        assertThat(updatedFlashCardDto).isNotNull();
        assertThat(updatedFlashCardDto.getQuestion()).isEqualTo(flashCardDto.getQuestion());
        assertThat(updatedFlashCardDto.getAnswer()).isEqualTo(flashCardDto.getAnswer());
        assertThat(updatedFlashCardDto.getTagIds()).containsExactlyInAnyOrder(tag2.getId());
    }

    @Test
    void givenFlashCardIdAndUserIdDeleteFlashCard() {
        when(mockFlashcardRepository.findByIdAndDeckUserId(flashCard.getId(), deck.getUser().getId())).thenReturn(Optional.ofNullable(flashCard));
        flashcardService.deleteFlashCard(user.getId(), flashCard.getId());
        verify(mockFlashcardRepository).delete(flashCard);
    }

    @Test
    void givenFlashCardIdAndUserIdReturnFlashCard() {
        when(mockFlashcardRepository.findByIdAndDeckUserId(flashCard.getId(), deck.getId())).thenReturn(Optional.ofNullable(flashCard));
        when(mockFlashcardMapper.mapTo(flashCard)).thenReturn(flashCardDto);
        FlashCardDto returnedFlashCardDto = flashcardService.getFlashCardByIdAndUser(flashCard.getId(), deck.getUser().getId()).get();
        verify(mockFlashcardRepository).findByIdAndDeckUserId(flashCard.getId(),deck.getUser().getId());
        assertThat(returnedFlashCardDto).isNotNull();
        assertThat(returnedFlashCardDto.getQuestion()).isEqualTo(flashCardDto.getQuestion());
        assertThat(returnedFlashCardDto.getAnswer()).isEqualTo(flashCardDto.getAnswer());
        assertThat(returnedFlashCardDto.getDeckId()).isEqualTo(flashCardDto.getDeckId());
        assertThat(returnedFlashCardDto.getTagIds()).containsExactlyInAnyOrder(tag.getId());
    }

//    @Test
//    void givenDeckIdAndUserIdReturnAllFlashCardsFromDeck() {
//        when(mockDeckService.getDeckByIdAndUser(deck.getId(),deck.getUser().getId())).thenReturn(Optional.ofNullable(deck));
//
//    }



    @Test
    void
}
