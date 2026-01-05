package com.myflashcardsapi.flashcards_api.controllers;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.services.impl.FlashCardServiceImpl;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class FlashCardController {
    private FlashCardServiceImpl flashCardService;

    public FlashCardController(FlashCardServiceImpl flashCardService) {

        this.flashCardService = flashCardService;
    }

    // --- CREATE ---
    @PostMapping("/decks/{deckId}/flashcards")
    public ResponseEntity<FlashCardDto> createFlashCard(@RequestBody FlashCardDto flashCardDto, @PathVariable Long deckId, @RequestHeader("X-User-ID") Long userId) throws BadRequestException {
        FlashCardDto flashCard = flashCardService.createFlashCard(userId,deckId,flashCardDto);
        return new ResponseEntity<>(flashCard, HttpStatus.CREATED);
    }

    // --- GET ---
    @GetMapping("/flashcards")
    public List<FlashCardDto> getAllUserFlashCards(@RequestHeader("X-User-ID") Long userId) {
        return flashCardService.getAllFlashCardsForUser(userId);
    }


    @GetMapping("/decks/{deckId}/flashcards")
    public List<FlashCardDto> getAllDeckFlashCards(@PathVariable Long deckId, @RequestHeader("X-User-ID") Long userId) {
        return flashCardService.getFlashCardsByDeckIdAndUser(deckId, userId);
    }

    @GetMapping("/flashcards/{flashCardId}")
    public ResponseEntity<FlashCardDto> getFlashCardById(@PathVariable Long flashCardId, @RequestHeader("X-User-ID") Long userId) {
        try {
            FlashCardDto flashCard = flashCardService.getFlashCardByIdAndUser(flashCardId, userId).get();
            return new ResponseEntity<>(flashCard,HttpStatus.OK);
        } catch(NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/flashcards/tags")
    public List<FlashCardDto> getFlashCardsByTags(@RequestHeader("X-User-ID") Long userId, @RequestParam List<Long> tagIds) throws BadRequestException {
        return flashCardService.getFlashCardsByTagsIdAndUser(tagIds, userId);
    }

    @GetMapping("/folders/{folderId}/flashcards")
    public List<FlashCardDto> getFlashCardsByFolderId(@RequestHeader("X-User-ID") Long userId, @PathVariable Long folderId) {
        return  flashCardService.getFlashCardsInFolder(folderId, userId);
    }
}
