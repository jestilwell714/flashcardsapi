package com.myflashcardsapi.flashcards_api.util;

import com.myflashcardsapi.flashcards_api.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Spring managed utility class that creates test entities
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TestEntityBuilder {

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Tag dataStructureTag;
    private Tag algorithmsTag;
    private Tag operatingSystemsTag;
    private Folder rootFolder;
    private Folder cosc201Folder;
    private Folder cosc204Folder;
    private Deck deck1;
    private Deck deck2;
    private Deck deck3;

    private Deck deck4;

    private FlashCard flashCard1;
    private FlashCard flashCard2;
    private FlashCard flashCard3;
    private FlashCard flashCard4;
    private FlashCard flashCard5;

    /**
     * Sets up test entities then stores them in instance variables
     */
    public void testEntitySetUp() {


        user = User.builder()
                .username("user")
                .password("password")
                .build();

        dataStructureTag = Tag.builder()
                .name("Data Structures")
                .user(user)
                .build();

        algorithmsTag = Tag.builder()
                .name("Algorithms")
                .user(user)
                .build();

        operatingSystemsTag = Tag.builder()
                .name("Operating Systems")
                .user(user)
                .build();

        rootFolder = Folder.builder()
                .name("2nd year")
                .user(user)
                .build();

        cosc201Folder = Folder.builder()
                .name("COSC201")
                .parentFolder(rootFolder)
                .user(user)
                .build();

        cosc204Folder = Folder.builder()
                .name("COSC204")
                .parentFolder(rootFolder)
                .user(user)
                .build();

        deck1 = Deck.builder()
                .name("Lecture 1")
                .folder(cosc201Folder)
                .user(user)
                .build();

        deck2 = Deck.builder()
                .name("Lecture 2")
                .folder(cosc201Folder)
                .user(user)
                .build();

        deck3 = Deck.builder()
                .name("Lecture 1")
                .folder(cosc204Folder)
                .user(user)
                .build();

        deck4 = Deck.builder()
                .name("Finance")
                .user(user)
                .build();

        flashCard1 = FlashCard.builder()
                .question("What is a stack?")
                .answer("A linear data structure that follows the LIFO (Last-In, First-Out) principle. Operations include Push (add) and Pop (remove).")
                .tags(Set.of(dataStructureTag))
                .deck(deck1)
                .build();

        flashCard2 = FlashCard.builder()
                .question("n which data structure is data organized into nodes, with each node having a value and pointers to other nodes?")
                .answer("Linked List")
                .tags(Set.of(dataStructureTag))
                .deck(deck1)
                .build();


        flashCard3 = FlashCard.builder()
                .question("What is the primary goal of a sorting algorithm?")
                .answer("To arrange elements of a list in a specific order")
                .tags(Set.of(algorithmsTag))
                .deck(deck1)
                .build();


        flashCard4 = FlashCard.builder()
                .question("Q: What is the common data structure often used to implement a Breadth-First Search (BFS) algorithm?")
                .answer("A queue")
                .tags(Set.of(dataStructureTag, algorithmsTag))
                .deck(deck2)
                .build();


        flashCard5 = FlashCard.builder()
                .question("What is the main purpose of an operating system (OS)?")
                .answer("To manage computer hardware and software resources, and provide common services for computer programs.")
                .deck(deck3)
                .build();

        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(dataStructureTag);
        entityManager.persistAndFlush(algorithmsTag);
        entityManager.persistAndFlush(operatingSystemsTag);
        entityManager.persistAndFlush(rootFolder);
        entityManager.persistAndFlush(cosc201Folder);
        entityManager.persistAndFlush(cosc204Folder);
        entityManager.persistAndFlush(deck1);
        entityManager.persistAndFlush(deck2);
        entityManager.persistAndFlush(deck3);
        entityManager.persistAndFlush(deck4);
        entityManager.persistAndFlush(flashCard1);
        entityManager.persistAndFlush(flashCard2);
        entityManager.persistAndFlush(flashCard3);
        entityManager.persistAndFlush(flashCard4);
        entityManager.persistAndFlush(flashCard5);
    }

}
