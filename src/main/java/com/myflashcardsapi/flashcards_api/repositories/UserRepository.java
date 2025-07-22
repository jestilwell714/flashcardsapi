package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by the username
    Optional<User> findByUsername(String username);

    // Find if a user exists by username
    boolean existsByUsername(String username);
}
