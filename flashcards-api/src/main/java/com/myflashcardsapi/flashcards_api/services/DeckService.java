package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.dto.DeckDto;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface DeckService {
    DeckDto createDeck(Long userId, DeckDto deckDto) throws BadRequestException;

    DeckDto updateDeck(Long userId, Long deckId, DeckDto deckDto) throws BadRequestException;

    void deleteDeck(Long userId, Long deckId);

    Optional<Deck> getDeckByIdAndUser(Long deckId, Long userId);

    Optional<DeckDto> getDeckDtoByIdAndUser(Long deckId, Long userId);
    List<DeckDto> getAllDeckDtosForUser(Long userId);

    List<DeckDto> getAllDeckDtosByFolderIdAndUser(Long folderId, Long userId);
}
