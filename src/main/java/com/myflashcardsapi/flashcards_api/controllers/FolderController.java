package com.myflashcardsapi.flashcards_api.controllers;

import com.myflashcardsapi.flashcards_api.services.impl.FolderServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FolderController {
    private FolderServiceImpl folderService;

    public FolderController(FolderServiceImpl folderService) {
        this.folderService = folderService;
    }

    // --- CREATE ---
    @PostMapping("folders")

}
