package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    // Finds a folder by its Id and the id of the user that owns it to ensure that a user can onky access there own folders
    Optional<Folder> findByIdAndUserId(Long folderId, Long userId);
    // Find all folders from a specific user
    List<Folder> findByUserId(Long userId);
    // Find all folders for a specific parent folder
    List<Folder> findByParentFolderIdAndUserId(Long folderId, Long userId);
    // Find all folders that do not have a parent folder (are at the root level)
    List<Folder> findByParentFolderIsNullAndUserId(Long userId);
    // Check if a folder with a specific name exists for a user
    boolean existsByNameIgnoreCaseAndParentFolderIdAndUserId(String name, Long parentFolderId, Long userId);

    boolean existsByNameIgnoreCaseAndParentFolderIsNullAndUserId(String name, Long userId);
}
