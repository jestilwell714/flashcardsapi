package com.myflashcardsapi.flashcards_api.util;

import com.myflashcardsapi.flashcards_api.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TestEntityBuilderUnit {
    private User user;
    private Deck deck1;
    private Deck deck2;
    private Deck deck3;
    private FlashCard flashCard1;
    private FlashCard flashCard2;
    private FlashCard flashCard3;
    private FlashCard flashCard4;
    private Tag dataStructureTag;
    private Tag algorithmsTag;
    private Folder cosc201Folder;
    private Folder cosc204Folder;
    private Folder rootFolder;
    public void testEntitySetUp() {
        this.user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        rootFolder = Folder.builder()
                .id(1L)
                .name("2nd year")
                .user(user)
                .build();

        cosc201Folder = Folder.builder()
                .id(2L)
                .name("COSC201")
                .parentFolder(rootFolder)
                .user(user)
                .build();

        cosc204Folder = Folder.builder()
                .id(3L)
                .name("COSC204")
                .parentFolder(rootFolder)
                .user(user)
                .build();

        this.dataStructureTag = Tag.builder()
                .id(1L)
                .name("Data Structures")
                .user(user)
                .build();

        this.algorithmsTag = Tag.builder()
                .id(2L)
                .name("Algorithms")
                .user(user)
                .build();


        this.deck1 = Deck.builder()
                .id(1L)
                .name("Lecture 1")
                .folder(cosc201Folder)
                .user(user)
                .build();

        this.deck2 = Deck.builder()
                .id(2L)
                .name("Lecture 2")
                .folder(cosc201Folder)
                .user(user)
                .build();

        this.deck3 = Deck.builder()
                .id(3L)
                .name("Lecture 1")
                .folder(cosc204Folder)
                .user(user)
                .build();

        this.flashCard1 = FlashCard.builder()
                .id(1L)
                .question("What is a stack?")
                .answer("A linear data structure that follows the LIFO (Last-In, First-Out) principle. Operations include Push (add) and Pop (remove).")
                .tags(List.of(dataStructureTag))
                .deck(deck1)
                .build();


        flashCard2 = FlashCard.builder()
                .id(2L)
                .question("n which data structure is data organized into nodes, with each node having a value and pointers to other nodes?")
                .answer("Linked List")
                .tags(List.of(dataStructureTag))
                .deck(deck1)
                .build();


        flashCard3 = FlashCard.builder()
                .id(3L)
                .question("What is the primary goal of a sorting algorithm?")
                .answer("To arrange elements of a list in a specific order")
                .tags(List.of(algorithmsTag))
                .deck(deck1)
                .build();


        flashCard4 = FlashCard.builder()
                .id(4L)
                .question("Q: What is the common data structure often used to implement a Breadth-First Search (BFS) algorithm?")
                .answer("A queue")
                .tags(List.of(dataStructureTag, algorithmsTag))
                .deck(deck2)
                .build();
    }
}
