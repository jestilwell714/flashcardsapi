package com.myflashcardsapi.flashcards_api.domain.dto;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;

    private String username;


    private String password;

    private List<Long> deckIds;

    private Set<Long> tagIds;
}
