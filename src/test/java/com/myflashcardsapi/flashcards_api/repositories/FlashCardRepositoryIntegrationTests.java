package com.myflashcardsapi.flashcards_api.repositories;
import com.myflashcardsapi.flashcards_api.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FlashCardRepositoryIntegrationTests {

    @Autowired
    private FlashCardRepository underTest;

    @Autowired
    private TestEntityManager entityManager;

    private User user;

    private Folder folder;
    private Deck deck;

    private Deck deck2;

    private Tag tag;

    private Tag tag2;
    private FlashCard flashCard1;

    private FlashCard flashCard2;

    private FlashCard flashCard3;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .username("user")
                .password("password")
                .build();

        tag = Tag.builder()
                .name("Algebra")
                .user(user)
                .build();

        tag2 = Tag.builder()
                .name("calculus")
                .user(user)
                .build();

        folder = Folder.builder()
                .name("Math")
                .user(user)
                .build();

        deck = Deck.builder()
                .name("Math202")
                .user(user)
                .folder(folder)
                .build();

        deck2 = Deck.builder()
                .name("Math303")
                .user(user)
                .folder(folder)
                .build();


        flashCard1 = FlashCard.builder()
                .question("what is 2 + 2")
                .answer("4")
                .deck(deck)
                .tags(Set.of(tag))
                .build();



        flashCard2 = FlashCard.builder()
                .question("what is 8 / 2")
                .answer("4")
                .deck(deck)
                .tags(Set.of(tag2))
                .build();

        flashCard3 = FlashCard.builder()
                .question("what is 3 squared")
                .answer("9")
                .deck(deck2)
                .tags(Set.of(tag))
                .build();

        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(folder);
        entityManager.persistAndFlush(tag);
        entityManager.persistAndFlush(tag2);
        entityManager.persistAndFlush(deck);
        entityManager.persistAndFlush(deck2);
        entityManager.persistAndFlush(flashCard1);
        entityManager.persistAndFlush(flashCard2);
        entityManager.persistAndFlush(flashCard3);
    }

    @Test
    void findByDeckIdShouldReturnAllFlashCardsForDeck() {
        List<FlashCard> flashCards = underTest.findByDeckId(deck.getId());
        assertThat(flashCards).hasSize(2);
        assertThat(flashCards).contains(flashCard1, flashCard2);
    }

    @Test
    void findByUserIdShouldReturnAllFlashCardsForUser() {
        List<FlashCard> flashCards = underTest.findByDeckUserId(user.getId());
        assertThat(flashCards).hasSize(3);
        assertThat(flashCards).contains(flashCard1, flashCard2, flashCard3);
    }

    @Test
    void findByTagIdShouldReturnAllFlashCardsForTag() {
        List<FlashCard> flashCards = underTest.findByTagsIdIn(List.of(tag.getId()));
        assertThat(flashCards).hasSize(2);
        assertThat(flashCards).contains(flashCard1, flashCard3);
    }

    @Test
    void findByTagIdShouldReturnAllFlashCardsForTags() {
        List<FlashCard> flashCards = underTest.findByTagsIdIn(List.of(tag.getId(), tag2.getId()));
        assertThat(flashCards).hasSize(3);
        assertThat(flashCards).contains(flashCard1, flashCard2, flashCard3);
    }

    @Test
    void findByFolderIdShouldReturnAllFlashCardsForFolder() {
        List<FlashCard> flashCards = underTest.findByDeckFolderId(folder.getId());
        assertThat(flashCards).hasSize(3);
        assertThat(flashCards).contains(flashCard1, flashCard2, flashCard3);
    }
}
