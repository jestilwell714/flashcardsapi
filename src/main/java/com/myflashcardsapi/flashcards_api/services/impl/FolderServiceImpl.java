package com.myflashcardsapi.flashcards_api.services.impl;

import com.myflashcardsapi.flashcards_api.domain.Folder;
import com.myflashcardsapi.flashcards_api.domain.dto.FolderDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.FolderMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.FolderRepository;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.FolderService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FolderServiceImpl implements FolderService {

    private UserRepository userRepository;
    private FolderRepository folderRepository;

    private FolderMapperImpl folderMapper;

    public FolderServiceImpl(FolderRepository folderRepository, UserRepository userRepository, FolderMapperImpl folderMapper) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.folderMapper = folderMapper;
    }


    @Override
    public FolderDto createFolder(Long userId, Long parentFolderId, FolderDto folderDto) {
        return null;
    }

    @Override
    public FolderDto updateFolder(Long userId, Long folderId, FolderDto folderDto) {
        return null;
    }

    @Override
    public void deleteFolder(Long userId, Long folderId) {

    }

    @Override
    public Optional<FolderDto> getFolderByIdAndUser(Long userId, Long folderId) {
        Optional<Folder> folder = folderRepository.findByIdAndUserId(folderId, userId);
        return folder.map(folderMapper::mapTo);
    }

    @Override
    public List<FolderDto> getAllFoldersForUser(Long userId) {
        userRepository.findById(userId).get();
        List<Folder> folders = folderRepository.findByUserId(userId);
        List<FolderDto> folderDtoList = new ArrayList<>();
        for(Folder folder : folders) {
            folderDtoList.add(folderMapper.mapTo(folder));
        }
        return folderDtoList;

    }

    @Override
    public List<FolderDto> getAllForParentFolderAndUser(Long folderId, Long userId) {
        List<Folder> folders = folderRepository.findByParentFolderIdAndUserId(folderId, userId);
        List<FolderDto> folderDtoList = new ArrayList<>();
        for(Folder folder : folders) {
            folderDtoList.add(folderMapper.mapTo(folder));
        }
        return folderDtoList;
    }

    @Override
    public List<FolderDto> getRootFoldersForUser(Long userId) {
        userRepository.findById(userId).get();
        List<Folder> folders = folderRepository.findByParentFolderIsNullAndUserId(userId);
        List<FolderDto> folderDtoList = new ArrayList<>();
        for(Folder folder : folders) {
            folderDtoList.add(folderMapper.mapTo(folder));
        }
        return folderDtoList;
    }

    @Override
    public List<Long> findAllDescendantFolderIds(Long folderId, Long userId) {
        Set<Long> ids = new HashSet<>();
        folderRepository.findByIdAndUserId(folderId, userId).get();
        ids.add(folderId);
        findChildrenFoldersRecursive(folderId,userId, ids);
        return new ArrayList<>(ids);
    }

    @Override
    public void findChildrenFoldersRecursive(Long parentId, Long userId, Set<Long> ids) {
        List<Folder> children = folderRepository.findByParentFolderIdAndUserId(parentId, userId);
        for(Folder child: children) {
            ids.add(child.getId());
            findChildrenFoldersRecursive(child.getId(), userId, ids);
        }
    }
}