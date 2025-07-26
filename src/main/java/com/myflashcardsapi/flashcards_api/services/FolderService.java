package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.Folder;

import java.util.List;
import java.util.Optional;

public interface FolderService {
    Folder createFolder(Long userId, Long parentFolderId, Folder folder);

    Folder updateFolder(Long userId, Folder folder);

    void deleteFolder(Long userId, Folder folder);

    Optional<Folder> getFolderByIdAndUser(Long userId, Long folderId);

    List<Folder> getAllFoldersForUser(Long userid);

    List<Folder> getAllForParentFolderAndUser(Long folderId, Long userId);

    List<Folder> getRootFoldersForUser(Long userId);
}
