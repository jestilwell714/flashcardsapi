package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestEntityBuilder.class)
public class DeckRepositoryIntegrationTests {
    @Autowired
    private TestEntityBuilder testEntityBuilder;
    @Autowired
    private DeckRepository underTest;

    @BeforeEach
    void setUp() {
        testEntityBuilder.testEntitySetUp();
    }

    @Test
    void findByUserIdShouldReturnAllDecksForUser() {
        List<Deck> decks = underTest.findByUserId(testEntityBuilder.getUser().getId());
        assertThat(decks).hasSize(4);
        assertThat(decks).contains(testEntityBuilder.getDeck1(), testEntityBuilder.getDeck2(), testEntityBuilder.getDeck3(), testEntityBuilder.getDeck4());
    }

    @Test
    void findByFolderIsNullAndUserIdShouldReturnAllDecksForRootFolder() {
        List<Deck> decks = underTest.findByFolderIsNullAndUserId(testEntityBuilder.getUser().getId());
        assertThat(decks).containsExactly(testEntityBuilder.getDeck4());
    }

    @Test
    void findByFolderUserAndUserIdShouldReturnAllDecksForFolder() {
        List<Deck> decks = underTest.findByFolderIdAndUserId(testEntityBuilder.getCosc201Folder().getId(), testEntityBuilder.getUser().getId());
        assertThat(decks).hasSize(2);
        assertThat(decks).contains(testEntityBuilder.getDeck1(), testEntityBuilder.getDeck2());
    }

    @Test
    void findByNameIgnoreCaseAndUserIdShouldReturnDeckForName() {
        List<Deck> deck = underTest.findByNameIgnoreCaseAndUserId(testEntityBuilder.getDeck1().getName(), testEntityBuilder.getUser().getId());
        assertThat(deck).hasSize(2);
        assertThat(deck).contains(testEntityBuilder.getDeck1(), testEntityBuilder.getDeck3());
    }

    @Test
    void existsByNameIgnoreCaseAndUserIdShouldReturnTrue() {
        boolean exists = underTest.existsByNameIgnoreCaseAndUserIdAndFolderId(testEntityBuilder.getDeck2().getName(), testEntityBuilder.getUser().getId(),testEntityBuilder.getCosc201Folder().getId());
        assertThat(exists).isTrue();
    }
}
