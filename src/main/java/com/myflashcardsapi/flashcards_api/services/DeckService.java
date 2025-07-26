package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.Deck;

import java.util.List;
import java.util.Optional;

public interface DeckService {
    Deck createDeck(Long userId, Deck deck);

    Deck updateDeck(Long userId, Deck deck);

    void deleteDeck(Long userId, Deck deck);

    Optional<Deck> getDeckByIdAndUserId(Long deckId, Long userId);

    Optional<Deck> getAllDecksForUser(Long userId);

    List<Deck> getDecksByFolderIdAndUser(Long folderId, Long userId);
}
