package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
    // Find all decks for a user
    List<Deck> findByUserId(Long userId);
    // Find a deck by its id and id of user who owns it
    Optional<Deck> findByIdAndUserId(Long deckId, Long userId);
    // Find all decks that have no parent folder (are on the root level)
    List<Deck> findByFolderIsNullAndUserId(Long userId);
    // Find all decks in a folder
    List<Deck> findByFolderIdAndUserId(Long folderId, Long userId);
    // Find decks by its name and the id of the user who owns it
    List<Deck> findByNameIgnoreCaseAndUserId(String name, Long userId);
    // Check if a deck with a specific deck exists
    boolean existsByNameIgnoreCaseAndUserId(String name, Long userId);
}
