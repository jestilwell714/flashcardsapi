package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, Long> {

    // Finds a flashcard by its id and user id, a more secure way to retrieve a singular flashcard
    Optional<FlashCard> findByIdAndDeckUserId(Long id, Long userId);

    // Find all flashcards for a certain deck and for a certain user
    List<FlashCard> findByDeckIdAndDeckUserId(Long deckId, Long userId);
    // FInd all flashcards for a user
    List<FlashCard> findByDeckUserId(Long userId);
    //Find all flashcards for a tag
    List<FlashCard> findByTagsIdInAndDeckUserId(List<Long> tagIds, Long userId);

    // Find all flashcards in a folder
    List<FlashCard> findByDeckFolderIdAndDeckUserId(Long folderId, Long userId);
}
