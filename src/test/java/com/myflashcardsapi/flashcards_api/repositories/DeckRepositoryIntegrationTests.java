package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

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
    void findByFolderUserIdShouldReturnAllDecksForFolder() {
        List<Deck> decks = underTest.findByFolderId(testEntityBuilder.getCosc201Folder().getId());
        assertThat(decks).hasSize(2);
        assertThat(decks).contains(testEntityBuilder.getDeck1(), testEntityBuilder.getDeck2());
    }
}
