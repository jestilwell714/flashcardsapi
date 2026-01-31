package com.myflashcardsapi.flashcards_api.controllers;

import com.myflashcardsapi.flashcards_api.domain.dto.DeckDto;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.services.DeckService;
import com.myflashcardsapi.flashcards_api.services.impl.DeckServiceImpl;
import com.myflashcardsapi.flashcards_api.services.impl.FlashCardServiceImpl;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class DeckController {
    private DeckService deckService;

    public DeckController(DeckService deckService) {

        this.deckService = deckService;
    }

    // --- CREATE ---
    @PostMapping("/decks")
    public ResponseEntity<DeckDto> createDeck(@RequestBody DeckDto deckDto, @RequestHeader("X-User-ID") Long userId) throws BadRequestException {
        DeckDto deck = deckService.createDeck(userId,deckDto);
        return new ResponseEntity<>(deck, HttpStatus.CREATED);
    }

    // --- READ ---
    @GetMapping("/decks/{deckId}")
    public ResponseEntity<DeckDto> getDeckById(@RequestHeader("X-User-ID") Long userId, @PathVariable Long deckId) {
        try {
            DeckDto deck = deckService.getDeckDtoByIdAndUser(deckId,userId).get();
            return new ResponseEntity<>(deck,HttpStatus.OK);
        } catch(NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/decks")
    public List<DeckDto> getAllDecksForUserId(@RequestHeader("X-User-ID") Long userId) {
        return  deckService.getAllDeckDtosForUser(userId);
    }

    @GetMapping("/folders/{folderId}/decks")
    public List<DeckDto> getAllDecksForFolderId(@RequestHeader("X-User-ID") Long userId, @PathVariable Long folderId) {
        return deckService.getAllDeckDtosByFolderIdAndUser(folderId,userId);
    }

    //--- UPDATE ---
    @PutMapping("/decks/{deckId}")
    public ResponseEntity<DeckDto> updateDeckById(@RequestBody DeckDto deckDto, @PathVariable Long deckId, @RequestHeader("X-User-ID") Long userId) throws BadRequestException {
        DeckDto deck = deckService.updateDeck(userId,deckId,deckDto);
        return new ResponseEntity<>(deck,HttpStatus.OK);
    }

    @DeleteMapping("/deck/{deckId}")
    public ResponseEntity<Void> deleteDeckById(@PathVariable Long deckId, @RequestHeader("X-User-ID") Long userId) {
        deckService.deleteDeck(userId,deckId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
