package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.dto.FolderDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FolderService {
    FolderDto createFolder(Long userId, Long parentFolderId, FolderDto folderDto);

    FolderDto updateFolder(Long userId, Long folderId, FolderDto folderDto);

    void deleteFolder(Long userId, Long folderId);

    Optional<FolderDto> getFolderByIdAndUser(Long userId, Long folderId);

    List<FolderDto> getAllFoldersForUser(Long userid);

    List<FolderDto> getAllForParentFolderAndUser(Long folderId, Long userId);

    List<FolderDto> getRootFoldersForUser(Long userId);

    List<Long> findAllDescendantFolderIds(Long folderId, Long userId);

    void findChildrenFoldersRecursive(Long parentId, Long userId, Set<Long> ids);
}
