package com.myflashcardsapi.flashcards_api.repositories;

import com.myflashcardsapi.flashcards_api.domain.Folder;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestEntityBuilder.class)
public class FolderRepositoryIntegrationTests {
        @Autowired
        private TestEntityBuilder testEntityBuilder;
        @Autowired
        private FolderRepository underTest;

        @BeforeEach
        void setUp() {
                testEntityBuilder.testEntitySetUp();
        }

        @Test
        void findByUserIdShouldReturnAllFoldersForUser() {
                List<Folder> folders= underTest.findByUserId(testEntityBuilder.getUser().getId());
                assertThat(folders).hasSize(3);
                assertThat(folders).contains(testEntityBuilder.getCosc201Folder(), testEntityBuilder.getCosc204Folder(), testEntityBuilder.getRootFolder());
        }

        @Test
        void findByParentFolderIdShouldReturnAllFoldersForParentFolder() {
                List<Folder> folders = underTest.findByParentFolderId(testEntityBuilder.getRootFolder().getId());
                assertThat(folders).hasSize(2);
                assertThat(folders).contains(testEntityBuilder.getCosc201Folder(), testEntityBuilder.getCosc204Folder());
        }

        @Test
        void findByParentFolderIsNullAndUserIdShouldReturnAllRootFolders() {
                List<Folder> folders = underTest.findByParentFolderIsNullAndUserId((testEntityBuilder.getUser().getId()));
                assertThat(folders).containsExactly(testEntityBuilder.getRootFolder());
        }
}
