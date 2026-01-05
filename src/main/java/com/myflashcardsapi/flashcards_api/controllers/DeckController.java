package com.myflashcardsapi.flashcards_api.controllers;

import com.myflashcardsapi.flashcards_api.domain.dto.DeckDto;
import com.myflashcardsapi.flashcards_api.services.impl.DeckServiceImpl;
import com.myflashcardsapi.flashcards_api.services.impl.FlashCardServiceImpl;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public class DeckController {
    private DeckServiceImpl deckService;

    public DeckController(DeckServiceImpl deckService) {

        this.deckService = deckService;
    }

    // --- POST ---
    @PostMapping("/decks/{deckId}")
    public ResponseEntity<DeckDto> createDeck(@RequestBody DeckDto deckDto, @RequestHeader("X-User-ID") Long userId) throws BadRequestException {
        DeckDto deck = deckService.createDeck(userId,deckDto);
        return new ResponseEntity<>(deck, HttpStatus.CREATED);
    }
}
