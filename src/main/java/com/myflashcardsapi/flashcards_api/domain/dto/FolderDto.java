package com.myflashcardsapi.flashcards_api.domain.dto;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.Folder;
import com.myflashcardsapi.flashcards_api.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FolderDto {

    private Long id;

    private String name;

    private User user;

    private Folder parentFolder;

    private List<Folder> childFolders;

    private List<Deck> decks;
}
