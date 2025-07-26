package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;

import java.util.List;
import java.util.Optional;

public interface FlashCardService {
    FlashCard createFlashCard(Long userId, Long deckId, FlashCard flashCard);

    FlashCard updateFlashCard(Long userId, FlashCard flashCard);

    void deleteFlashCard(Long userId, Long flashCardId);

    Optional<FlashCard> getFlashCardByIdAndUser(Long flashcardId, Long userId);

    List<FlashCard> GetDeckByIdAndUser(Long deckId, Long userId);

    List<FlashCard> getAllFlashCardsForUser(Long userId);

    List<FlashCard> getFlashCardsByTagsIdAndUser(List<Long> tagIds, Long userId);

    List<FlashCard> getFlashCardsInFolder(Long folderId, Long userId);
}
