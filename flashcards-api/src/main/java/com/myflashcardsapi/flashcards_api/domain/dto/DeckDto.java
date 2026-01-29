package com.myflashcardsapi.flashcards_api.domain.dto;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
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
public class DeckDto {

    private Long id;

    private String name;

    private Long userId;

    private Long folderId;

    private List<Long> flashCardIds;
}
