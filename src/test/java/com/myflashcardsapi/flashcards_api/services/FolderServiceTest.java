package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.Folder;
import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.domain.dto.FolderDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.FolderMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.FolderRepository;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.impl.FolderServiceImpl;
import com.myflashcardsapi.flashcards_api.util.TestEntityBuilderUnit;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FolderServiceTest {
    @Mock
    private FolderRepository mockFolderRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private FolderMapperImpl mockFolderMapper;

    @InjectMocks
    private FolderServiceImpl folderService;

    private User user;
    private Folder folder;
    private FolderDto folderDto;
    private Folder folder2;
    private FolderDto folderDto2;
    private Folder folder3;
    private Folder folder4;


    private TestEntityBuilderUnit testEntityBuilder;

    @BeforeEach
    void setUp() {
        testEntityBuilder = new TestEntityBuilderUnit();
        testEntityBuilder.testEntitySetUp();
        user = testEntityBuilder.getUser();
        folder = testEntityBuilder.getRootFolder();
        folder2 = testEntityBuilder.getCosc201Folder();
        folder3 = testEntityBuilder.getCosc204Folder();
        folder4 = testEntityBuilder.getCosc201MidTermFolder();

        folderDto = new FolderDto();
        folderDto.setId(folder.getId());
        folderDto.setName(folder.getName());
        folderDto.setUserId(folder.getUser().getId());
        folderDto.setChildFoldersIds(List.of(folder2.getId(),folder3.getId()));

        folderDto2 = new FolderDto();
        folderDto2.setId(folder2.getId());
        folderDto2.setName(folder2.getName());
        folderDto2.setUserId(user.getId());
        folderDto2.setParentFolderId(folder.getId());
    }

    @Test
    void givenDtoWhenCreateFolderReturnDto() throws BadRequestException {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockFolderRepository.findByIdAndUserId(folder.getId(), user.getId())).thenReturn(Optional.of(folder));
        when(mockFolderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(folder2.getName(),folder.getId(), user.getId())).thenReturn(false);
        when(mockFolderMapper.mapFrom(folderDto2)).thenReturn(folder2);
        when(mockFolderRepository.save(folder2)).thenReturn(folder2);
        when(mockFolderMapper.mapTo(folder2)).thenReturn(folderDto2);

        FolderDto createdFolder = folderService.createFolder(user.getId(),folder.getId(),folderDto2);
        assert(createdFolder.getName().equals(folderDto2.getName()));
        assert(createdFolder.getId().equals(folderDto2.getId()));
        assert(createdFolder.getParentFolderId().equals(folderDto2.getParentFolderId()));
        verify(mockFolderRepository).save(folder2);
    }

    @Test
    void givenDtoWithSameNameWhenCreateFolderThrowException() {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockFolderRepository.findByIdAndUserId(folder.getId(), user.getId())).thenReturn(Optional.of(folder));
        when(mockFolderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(folder2.getName(),folder.getId(), user.getId())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> folderService.createFolder(user.getId(),folder.getId(),folderDto2));
    }

    @Test
    void givenDtoWhenUpdateFolderVerifyFolderModified() throws BadRequestException {
        folderDto2.setName("COSC202");
        when(mockFolderRepository.findByIdAndUserId(folder2.getId(), user.getId())).thenReturn(Optional.of(folder2));
        when(mockFolderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(folderDto2.getName(),folder.getId(), user.getId())).thenReturn(false);
        when(mockFolderRepository.findByIdAndUserId(folder.getId(), user.getId())).thenReturn(Optional.of(folder));
        when(mockFolderRepository.save(folder2)).thenReturn(folder2);
        when(mockFolderMapper.mapTo(folder2)).thenReturn(folderDto2);

        FolderDto updatedFolderDto = folderService.updateFolder(user.getId(), folder2.getId(),folderDto2);
        assertThat(updatedFolderDto).isNotNull();
        assert (folder2.getName().equals("COSC202"));
        verify(mockFolderRepository).save(folder2);
    }

    @Test
    void givenDtoWithSameNameWhenUpdateFolderThrowsException() throws BadRequestException {
        folderDto2.setName("COSC204");
        when(mockFolderRepository.findByIdAndUserId(folder2.getId(), user.getId())).thenReturn(Optional.of(folder2));
        when(mockFolderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(folderDto2.getName(),folder.getId(), user.getId())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> folderService.updateFolder(user.getId(),folder2.getId(),folderDto2));
    }

    @Test
    void givenDtoWhenMoveFolderFromRootIntoDescendantThrowsException() {
        folderDto.setParentFolderId(folder2.getId());
        when(mockFolderRepository.findByIdAndUserId(folder.getId(), user.getId())).thenReturn(Optional.of(folder));
        assertThrows(BadRequestException.class, () -> folderService.updateFolder(user.getId(),folder.getId(),folderDto));
    }

    @Test
    void givenDtoWhenMoveFolderIntoDescendantThrowsException() {
        folderDto2.setParentFolderId(folder4.getId());
        when(mockFolderRepository.findByIdAndUserId(folder2.getId(), user.getId())).thenReturn(Optional.of(folder2));
        when(mockFolderRepository.findByParentFolderIdAndUserId(folder2.getId(), user.getId())).thenReturn(List.of(folder4));
        when(mockFolderRepository.findByParentFolderIdAndUserId(folder4.getId(), user.getId())).thenReturn(null);
        assertThrows(BadRequestException.class, () -> folderService.updateFolder(user.getId(),folder2.getId(),folderDto2));
    }


    @Test
    void givenFolderIdAndUserIDWhenDeleteFolderVerifyMethodCall() {
        when(mockFolderRepository.findByIdAndUserId(folder.getId(), user.getId())).thenReturn(Optional.of(folder));
        folderService.deleteFolder(user.getId(), folder.getId());
        verify(mockFolderRepository).delete(folder);
    }

    @Test
    void givenUserIdWhenFindAllRootFoldersReturnListOfRootFolderDtos() {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockFolderRepository.findByParentFolderIsNullAndUserId(user.getId())).thenReturn(List.of(folder));
        when(mockFolderMapper.mapTo(folder)).thenReturn(folderDto);

        List<FolderDto> folderDtoList = folderService.getRootFoldersForUser(user.getId());
        assert(folderDtoList.containsAll(List.of(folderDto)));
    }

    @Test
    void givenFolderIdWhenFindAllDescendantFolderIdsReturnListOfFolderIds(){
        when(mockFolderRepository.findByIdAndUserId(folder.getId(), user.getId())).thenReturn(Optional.of(folder));
        when(mockFolderRepository.findByParentFolderIdAndUserId(folder.getId(), user.getId())).thenReturn(List.of(folder2,folder3));
        when(mockFolderRepository.findByParentFolderIdAndUserId(folder2.getId(), user.getId())).thenReturn(List.of(folder4));
        when(mockFolderRepository.findByParentFolderIdAndUserId(folder3.getId(), user.getId())).thenReturn(null);
        when(mockFolderRepository.findByParentFolderIdAndUserId(folder4.getId(), user.getId())).thenReturn(null);
        List<Long> folderIdList = folderService.findAllDescendantFolderIds(folder.getId(), user.getId());

        assert(folderIdList.containsAll(List.of(folder.getId(),folder2.getId(),folder3.getId(),folder4.getId())));
    }


}
