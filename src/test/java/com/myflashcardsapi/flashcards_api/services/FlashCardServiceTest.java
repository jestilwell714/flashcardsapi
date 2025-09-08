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
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilder;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilderUnit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void givenDtoWhenUpdateFlashcardThenRetuenUpdateDto() throws BadRequestException {
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
}
