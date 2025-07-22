package com.myflashcardsapi.flashcards_api.repositories;


import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(TestEntityBuilder.class)
public class UserRepositoryIntegrationTests {
    @Autowired
    private TestEntityBuilder testEntityBuilder;
    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void setUp() {
        testEntityBuilder.testEntitySetUp();
    }

    @Test
    void findByUsernameShouldReturnUser() {
        Optional<User> user = underTest.findByUsername(testEntityBuilder.getUser().getUsername());
        assertThat(user).contains(testEntityBuilder.getUser());
    }

    @Test
    void existsByUsernameShouldReturnTrue() {
        boolean exists = underTest.existsByUsername(testEntityBuilder.getUser().getUsername());
        assertThat(exists).isTrue();
    }
}
