package com.myflashcardsapi.flashcards_api.repositories;
import com.myflashcardsapi.flashcards_api.domain.*;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestEntityBuilder.class)
public class FlashCardRepositoryIntegrationTests {
    @Autowired
    private TestEntityBuilder testEntityBuilder;
    @Autowired
    private FlashCardRepository underTest;

    @BeforeEach
    void setUp() {

        testEntityBuilder.testEntitySetUp();
    }

    @Test
    void findByDeckIdShouldReturnAllFlashCardsForDeck() {
        List<FlashCard> flashCards = underTest.findByDeckId(testEntityBuilder.getDeck1().getId());
        assertThat(flashCards).hasSize(3);
        assertThat(flashCards).contains(testEntityBuilder.getFlashCard1(), testEntityBuilder.getFlashCard2(), testEntityBuilder.getFlashCard3());
    }

    @Test
    void findByUserIdShouldReturnAllFlashCardsForUser() {
        List<FlashCard> flashCards = underTest.findByDeckUserId(testEntityBuilder.getUser().getId());
        assertThat(flashCards).hasSize(5);
        assertThat(flashCards).contains(testEntityBuilder.getFlashCard1(), testEntityBuilder.getFlashCard2(), testEntityBuilder.getFlashCard3(), testEntityBuilder.getFlashCard4(), testEntityBuilder.getFlashCard5());
    }

    @Test
    void findByTagIdShouldReturnAllFlashCardsForTag() {
        List<FlashCard> flashCards = underTest.findByTagsIdIn(List.of(testEntityBuilder.getDataStructureTag().getId()));
        assertThat(flashCards).hasSize(3);
        assertThat(flashCards).contains(testEntityBuilder.getFlashCard1(), testEntityBuilder.getFlashCard2(), testEntityBuilder.getFlashCard4());
    }

    @Test
    void findByTagIdShouldReturnAllFlashCardsForTags() {
        List<FlashCard> flashCards = underTest.findByTagsIdIn(List.of(testEntityBuilder.getDataStructureTag().getId(), testEntityBuilder.getAlgorithmsTag().getId()));
        assertThat(flashCards).hasSize(4);
        assertThat(flashCards).contains(testEntityBuilder.getFlashCard1(), testEntityBuilder.getFlashCard2(), testEntityBuilder.getFlashCard3(), testEntityBuilder.getFlashCard4());
    }

    @Test
    void findByFolderIdShouldReturnAllFlashCardsForFolder() {
        List<FlashCard> flashCards = underTest.findByDeckFolderId(testEntityBuilder.getCosc201Folder().getId());
        assertThat(flashCards).hasSize(4);
        assertThat(flashCards).contains(testEntityBuilder.getFlashCard1(), testEntityBuilder.getFlashCard2(), testEntityBuilder.getFlashCard3(), testEntityBuilder.getFlashCard4());
    }
}
