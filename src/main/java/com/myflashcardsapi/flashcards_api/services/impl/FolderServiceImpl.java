package com.myflashcardsapi.flashcards_api.services.impl;

import com.myflashcardsapi.flashcards_api.domain.Folder;
import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.domain.dto.FolderDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.FolderMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.FolderRepository;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.FolderService;
import org.apache.coyote.BadRequestException;
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
    public FolderDto createFolder(Long userId, Long parentFolderId, FolderDto folderDto) throws BadRequestException {
        User user = userRepository.findById(userId).get();

        Folder parentFolder = null;
        if(folderDto.getParentFolderId() != null) {
            parentFolder = folderRepository.findByIdAndUserId(folderDto.getParentFolderId(), userId).get();
        }

        boolean nameExists;
        if (folderDto.getParentFolderId() == null) {
            nameExists = folderRepository.existsByNameIgnoreCaseAndParentFolderIsNullAndUserId(folderDto.getName(), userId);
        } else {
            nameExists = folderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(folderDto.getName(), folderDto.getParentFolderId(), userId);
        }

        if(nameExists) {
            throw new BadRequestException("Folder with name " + folderDto.getName() + " already exists in this directory");
        }

        Folder folder = folderMapper.mapFrom(folderDto);
        folder.setUser(user);
        folder.setParentFolder(parentFolder);

        Folder savedFolder = folderRepository.save(folder);
        return  folderMapper.mapTo(folder);
    }

    @Override
    public FolderDto updateFolder(Long userId, Long folderId, FolderDto folderDto) throws BadRequestException {
        Folder folder = folderRepository.findByIdAndUserId(folderId, userId).get();
        boolean parentHasChanged = false;
        boolean nameChanged = !folder.getName().equalsIgnoreCase(folderDto.getName());

        //Check if folder has been moved into another parent folder
        if(folder.getParentFolder() != null && folderDto.getParentFolderId() != null) {
            if (!Optional.ofNullable(folderDto.getParentFolderId()).equals(folder.getParentFolder().getId())) {
                // Prevent setting itself or its descendants as parent
                List<Long> existingFolderDescendants = findAllDescendantFolderIds(folder.getId(), userId);
                if (existingFolderDescendants.contains(folderDto.getParentFolderId())) {
                    throw new BadRequestException("Cannot move a folder into itself or its own descendants.");
                }

                parentHasChanged = true;
            }
        } else if(folder.getParentFolder() == null && folderDto.getParentFolderId() != null) {
            throw new BadRequestException("Cannot move a folder into itself or its own descendants.");
        } else if(folderDto.getParentFolderId() == null && folderDto.getParentFolderId() != null) {

            parentHasChanged = true;
        }

        // Check if name is unique in directory level
        if(nameChanged || parentHasChanged) {
            if(folderDto.getParentFolderId() == null) {
                if(folderRepository.existsByNameIgnoreCaseAndParentFolderIsNullAndUserId(folderDto.getName(),userId)) {
                    throw new BadRequestException("Folder with name " + folderDto.getName() + " is already in this directory");
                }
                folder.setParentFolder(null);
            } else {
                if(folderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(folderDto.getName(), folderDto.getParentFolderId(), userId)) {
                    throw new BadRequestException("Folder with name " + folderDto.getName() + " is already in this directory");
                }
                Folder parent = folderRepository.findByIdAndUserId(folderDto.getParentFolderId(), userId).get();
                folder.setParentFolder(parent);
            }
            folder.setName(folderDto.getName());
        }

        Folder updatedFolder = folderRepository.save(folder);
        return folderMapper.mapTo(updatedFolder);
    }

    @Override
    public void deleteFolder(Long userId, Long folderId) {
        Folder folder = folderRepository.findByIdAndUserId(folderId, userId).get();
        folderRepository.delete(folder);
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
        if(children != null) {
            for (Folder child : children) {
                ids.add(child.getId());
                findChildrenFoldersRecursive(child.getId(), userId, ids);
            }
        }
    }
}