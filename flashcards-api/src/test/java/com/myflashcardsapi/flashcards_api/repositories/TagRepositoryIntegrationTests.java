package com.myflashcardsapi.flashcards_api.repositories;


import com.myflashcardsapi.flashcards_api.domain.Tag;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestEntityBuilder.class)
public class TagRepositoryIntegrationTests {

    @Autowired
    private TestEntityBuilder testEntityBuilder;
    @Autowired
    private TagRepository underTest;

    @BeforeEach
    void setUp() {
        testEntityBuilder.testEntitySetUp();
    }

    @Test
    void findByUserIdShouldReturnAllTagsForUser() {
        List<Tag> tags = underTest.findByUserId(testEntityBuilder.getUser().getId());
        assertThat(tags).hasSize(3);
        assertThat(tags).contains(testEntityBuilder.getDataStructureTag(), testEntityBuilder.getAlgorithmsTag(), testEntityBuilder.getOperatingSystemsTag());
    }

    @Test
    void findByNameIgnoreCaseAndUserIdShouldReturnTagWithName() {
        Optional<Tag> tag = underTest.findByNameIgnoreCaseAndUserId(testEntityBuilder.getDataStructureTag().getName(), testEntityBuilder.getUser().getId());
        assertThat(tag).contains( testEntityBuilder.getDataStructureTag());
    }

    @Test
    void findByIdAndUserIdShouldReturnTag() {
        Optional<Tag> tag = underTest.findByIdAndUserId(testEntityBuilder.getAlgorithmsTag().getId(), testEntityBuilder.getUser().getId());
        assertThat(tag).contains(testEntityBuilder.getAlgorithmsTag());
    }

    @Test
    void findByNameInAndUserIdShouldReturnAllTagsWithNamesFromList() {
        List<Tag> tags = underTest.findByNameInAndUserId(List.of(testEntityBuilder.getDataStructureTag().getName(), testEntityBuilder.getAlgorithmsTag().getName()), testEntityBuilder.getUser().getId());
        assertThat(tags).containsExactly(testEntityBuilder.getDataStructureTag(), testEntityBuilder.getAlgorithmsTag());
    }

    @Test
    void existsByNameIgnoreCaseAndUserIdShouldReturnTrue() {
        boolean exists = underTest.existsByNameIgnoreCaseAndUserId(testEntityBuilder.getOperatingSystemsTag().getName(), testEntityBuilder.getUser().getId());
        assertThat(exists).isTrue();
    }

    @Test
    void existsByNameIgnoreCaseAndUserIdShouldReturnFalse() {
        boolean exists = underTest.existsByNameIgnoreCaseAndUserId("Security", testEntityBuilder.getUser().getId());
        assertThat(exists).isFalse();
    }
}
