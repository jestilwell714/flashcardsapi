package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, Long> {

    // Find all flashcards for a certain deck
    List<FlashCard> findByDeckId(Long deckId);
    // FInd all flashcards for a user
    List<FlashCard> findByDeckUserId(Long userId);
    //Find all flashcards for a tag
    List<FlashCard> findByTagsIdIn(List<Long> tagIds);

    // Find all flashcards in a folder
    List<FlashCard> findByDeckFolderId(Long folderId);
}
