package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import org.apache.coyote.BadRequestException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FlashCardService {
    FlashCardDto createFlashCard(Long userId, Long deckId, FlashCardDto flashCardDto) throws BadRequestException;

    @Transactional
    void updateWeight(Long id, Long userId, int score);

    FlashCardDto updateFlashCard(Long userId, Long flashCardId, FlashCardDto flashCardDto) throws BadRequestException;

    void deleteFlashCard(Long userId, Long flashCardId);

    Optional<FlashCardDto> getFlashCardByIdAndUser(Long flashcardId, Long userId);

    List<FlashCardDto> getFlashCardsByDeckIdAndUser(Long deckId, Long userId);

    List<FlashCardDto> getAllFlashCardsForUser(Long userId);

    List<FlashCardDto> getFlashCardsByTagsIdAndUser(List<Long> tagIds, Long userId) throws BadRequestException;

    List<FlashCardDto> getFlashCardsInFolder(Long folderId, Long userId);

    List<FlashCardDto> getFlashCardsForCramByDeckId(Long userId, Long deckId);

    List<FlashCardDto> getFlashCardsForCramByFolderId(Long userId, Long folderId);

    List<FlashCardDto> getFlashCardsForCramByTagsId(Long userId, List<Long> tagIds) throws BadRequestException;
}
