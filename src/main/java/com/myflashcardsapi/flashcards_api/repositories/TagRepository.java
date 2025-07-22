package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // Find all tags for a specific user
    List<Tag> findByUserId(Long userId);
    // Find by name ignoring the case
    Optional<Tag> findByNameIgnoreCaseAndUserId(String name, Long userId);
    // Finds all the tags for its id, ensuring that its the user who owns it
    Optional<Tag> findByIdAndUserId(Long id, Long userId);
    // Find all tags belonging to a specific user and whose name are in a given list
    List<Tag> findByNameInAndUserId(List<String> names, Long userId);
    // checks if a tag with a specific name exists for a given user
    boolean existsByNameIgnoreCaseAndUserId(String name, Long userId);
}
