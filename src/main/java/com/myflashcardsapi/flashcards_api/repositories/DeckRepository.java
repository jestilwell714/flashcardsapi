package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {
    // Find all decks for a user
    List<Deck> findByUserId(Long userId);
    // Find all decks in a folder
    //List<Deck> findByParentFolder(Long )
}
