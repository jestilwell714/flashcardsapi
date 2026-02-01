package com.myflashcardsapi.flashcards_api.mappers.impl;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Implementation class that implements Mapper and maps FlashCard and FlashCardDto objects
 */
@Component
public class FlashCardMapperImpl implements Mapper<FlashCardDto, FlashCard> {

    private ModelMapper modelMapper;

    public FlashCardMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * We want to map a FlashCard object to a FlashCardDto
     * @param flashCard
     * @return the mapped FlashCardDto object
     */
    @Override
    public FlashCardDto mapTo(FlashCard flashCard) {
        return modelMapper.map(flashCard, FlashCardDto.class);
    }

    /**
     * We want to map a FlashCardDto object to a FlashCard
     * @param flashCardDto
     * @return the mapped FlashCard object
     */
    @Override
    public FlashCard mapFrom(FlashCardDto flashCardDto) {
        return modelMapper.map(flashCardDto, FlashCard.class);
    }
}
