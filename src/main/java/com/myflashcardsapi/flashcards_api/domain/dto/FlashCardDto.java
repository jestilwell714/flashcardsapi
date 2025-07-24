package com.myflashcardsapi.flashcards_api.domain.dto;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashCardDto {

    private Long id;

    private String question;

    private String answer;

    private Set<Tag> tags = new HashSet<>();

    private Deck deck;

    private int difficultyLevel;
}
