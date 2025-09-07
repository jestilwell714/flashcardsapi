package com.myflashcardsapi.flashcards_api.util;

import com.myflashcardsapi.flashcards_api.domain.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestEntityBuilderUnit {
    private User user;
    private Deck deck1;
    private FlashCard flashCard1;
    private Tag dataStructureTag;

    private Folder cosc201Folder;

    private Folder rootFolder;

    public void testEntitySetup() {
        this.user = User.builder()
                .id(1L)
                .username("testuser")
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

        this.dataStructureTag = Tag.builder()
                .id(1L)
                .name("Data Structures")
                .user(user)
                .build();


        this.deck1 = Deck.builder()
                .id(1L)
                .folder(cosc201Folder)
                .user(user)
                .build();

        this.flashCard1 = FlashCard.builder()
                .id(1L)
                .question("What is a stack?")
                .answer("A linear data structure that follows the LIFO (Last-In, First-Out) principle. Operations include Push (add) and Pop (remove).")
                .tags(List.of(dataStructureTag))
                .deck(deck1)
                .build();
    }
}
