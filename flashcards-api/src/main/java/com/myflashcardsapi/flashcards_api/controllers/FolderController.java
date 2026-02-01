package com.myflashcardsapi.flashcards_api.controllers;

import com.myflashcardsapi.flashcards_api.domain.dto.FolderDto;
import com.myflashcardsapi.flashcards_api.domain.dto.ItemDto;
import com.myflashcardsapi.flashcards_api.services.FolderService;
import com.myflashcardsapi.flashcards_api.services.impl.FolderServiceImpl;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class FolderController {
    private FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    // --- CREATE ---
    @PostMapping("/folders/{parentFolderId}")
    public ResponseEntity<FolderDto> createFolder(@RequestBody FolderDto folderDto,@PathVariable Long parentFolderId, @RequestHeader("X-User-ID") Long userId) throws BadRequestException {
        FolderDto folder = folderService.createFolder(userId,parentFolderId,folderDto);
        return new ResponseEntity<>(folder, HttpStatus.CREATED);
    }

    // --- READ ---
    @GetMapping("/folders")
    public List<FolderDto> getAllRootFolders(@RequestHeader("X-User-ID") Long userId) {
        return folderService.getRootFoldersForUser(userId);
    }

    @GetMapping("/folders/{parentFolderId}/subfolders")
    public List<FolderDto> getAllfoldersByParentFolderId(@RequestHeader("X-User-ID") Long userId, @PathVariable Long parentFolderId) {
        return folderService.getAllForParentFolderAndUser(parentFolderId,userId);
    }

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<FolderDto> getFolderByFolderId(@PathVariable Long folderId, @RequestHeader("X-User-ID") Long userId) {
        try {
            FolderDto folder = folderService.getFolderByIdAndUser(folderId,userId).get();
            return new ResponseEntity<>(folder,HttpStatus.OK);
        } catch(NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/content")
    public List<ItemDto> getContentsByRoot(@RequestHeader("X-User-ID") Long userId) {
        return folderService.getRootContents(userId);
    }

    @GetMapping("/content/{folderId")
    public List<ItemDto> getContentsByFolderId(@PathVariable Long folderId, @RequestHeader("X-User-ID") Long userId) {
        return folderService.getFolderContents(folderId,userId);
    }

    // --- UPDATE ---
    @PutMapping("/folders/{folderId}")
    public ResponseEntity<FolderDto> updateFolder(@PathVariable Long folderId, @RequestHeader("X-User-ID") Long userId, @RequestHeader FolderDto folderDto) throws BadRequestException {
        FolderDto folder = folderService.updateFolder(userId,folderId,folderDto);
        return new ResponseEntity<>(folder,HttpStatus.OK);
    }

    // --- DELETE ---
    @DeleteMapping("/folders/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId, @RequestHeader("X-User-ID") Long userId) {
        folderService.deleteFolder(userId,folderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
