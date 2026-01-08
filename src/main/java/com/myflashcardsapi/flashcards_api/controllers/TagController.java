package com.myflashcardsapi.flashcards_api.controllers;

import com.myflashcardsapi.flashcards_api.domain.dto.TagDto;
import com.myflashcardsapi.flashcards_api.services.TagService;
import com.myflashcardsapi.flashcards_api.services.impl.TagServiceImpl;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class TagController {
    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // --- CREATE ---

    @PostMapping("/tags")
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto, @RequestHeader("X-User-ID") Long userId) throws BadRequestException {
        TagDto tag = tagService.createTag(userId,tagDto);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    // --- READ ---
    @GetMapping("/tags/{tagId}")
    public ResponseEntity<TagDto> getTagByTagId(@PathVariable Long tagId, @RequestHeader("X-User-Id") Long userId) {
        try {
            TagDto tag = tagService.getTagByIdAndUser(tagId,userId).get();
            return new ResponseEntity<>(tag,HttpStatus.OK);
        } catch(NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tags")
    public List<TagDto> getTagsByUserId(@RequestHeader("X-User-ID") Long userId) {
        return tagService.getAllTagsForUser(userId);
    }

    // --- UPDATE ---
    @PutMapping("/tags/{tagId}")
    public ResponseEntity<TagDto> updateTag(@RequestBody TagDto tagDto, @PathVariable Long tagId, @RequestHeader("X-User-ID") Long userId) throws BadRequestException {
        TagDto tag = tagService.updateTag(userId,tagId,tagDto);
        return new ResponseEntity<>(tag,HttpStatus.OK);
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<Void> deleteTag(@RequestHeader("X-User-ID") Long userId, @PathVariable Long tagId) {
        tagService.deleteTag(userId,tagId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
