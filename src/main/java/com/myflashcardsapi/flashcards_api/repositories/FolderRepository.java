package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    // Find all folders from a specific user
    List<Folder> findByUserId(Long userId);
    // Find all folders for a specific parent folder
    List<Folder> findByParentFolderId(Long folderId);
    // Find all folders that do not have a parent folder (are at the root level)
    List<Folder> findByParentFolderIsNullAndUserId(Long userId);
}
