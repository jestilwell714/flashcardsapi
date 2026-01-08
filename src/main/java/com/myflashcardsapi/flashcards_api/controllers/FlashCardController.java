package com.myflashcardsapi.flashcards_api.controllers;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.services.FlashCardService;
import com.myflashcardsapi.flashcards_api.services.impl.FlashCardServiceImpl;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class FlashCardController {
    private FlashCardService flashCardService;

    public FlashCardController(FlashCardService flashCardService) {

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

    // --- UPDATE ---
    @PutMapping("/flashcards/{flashCardId}")
    public ResponseEntity<FlashCardDto> updateFlashCard(@RequestBody FlashCardDto flashCardDto, @RequestHeader("X-User-ID") Long userId, @PathVariable Long flashCardId) throws BadRequestException {
        FlashCardDto flashcard = flashCardService.updateFlashCard(userId,flashCardId,flashCardDto);
        return new ResponseEntity<>(flashcard,HttpStatus.OK);
    }

    // --- DELETE ---
    @DeleteMapping("/flashcards/{flashCardId}")
    public ResponseEntity<Void> deleteFlashCard(@RequestHeader("X-User-ID") Long userId,
                                                        @PathVariable Long flashCardId) {
        flashCardService.deleteFlashCard(userId,flashCardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
