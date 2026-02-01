package com.myflashcardsapi.flashcards_api.mappers.impl;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.Folder;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.domain.dto.FolderDto;
import com.myflashcardsapi.flashcards_api.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Implementation class that implements Mapper and maps Folder and FolderDto objects
 */
@Component
public class FolderMapperImpl implements Mapper<FolderDto, Folder> {

    private ModelMapper modelMapper;

    public FolderMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * We want to map a FolderDto object to a Folder
     * @param folder
     * @return the mapped Folder object
     */
    @Override
    public FolderDto mapTo(Folder folder) {
        return modelMapper.map(folder, FolderDto.class);
    }

    /**
     * We want to map a FolderDto object to a Folder
     * @param folderDto
     * @return the mapped Folder object
     */
    @Override
    public Folder mapFrom(FolderDto folderDto) {
        return modelMapper.map(folderDto, Folder.class);
    }

}
