package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.dto.FolderDto;
import com.myflashcardsapi.flashcards_api.domain.dto.ItemDto;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FolderService {
    FolderDto createFolder(Long userId, Long parentFolderId, FolderDto folderDto) throws BadRequestException;

    FolderDto updateFolder(Long userId, Long folderId, FolderDto folderDto) throws BadRequestException;

    void deleteFolder(Long userId, Long folderId);

    Optional<FolderDto> getFolderByIdAndUser(Long userId, Long folderId);

    List<FolderDto> getAllFoldersForUser(Long userid);

    List<FolderDto> getAllForParentFolderAndUser(Long folderId, Long userId);

    List<FolderDto> getRootFoldersForUser(Long userId);

    List<Long> findAllDescendantFolderIds(Long folderId, Long userId);

    void findChildrenFoldersRecursive(Long parentId, Long userId, Set<Long> ids);

    List<ItemDto> getFolderContents(Long folderId, Long userId);

    List<ItemDto> getRootContents(Long userId);
}
