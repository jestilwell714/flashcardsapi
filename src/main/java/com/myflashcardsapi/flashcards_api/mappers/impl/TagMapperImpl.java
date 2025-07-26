package com.myflashcardsapi.flashcards_api.mappers.impl;

import com.myflashcardsapi.flashcards_api.domain.Tag;
import com.myflashcardsapi.flashcards_api.domain.dto.TagDto;
import com.myflashcardsapi.flashcards_api.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Implementation class that implements Mapper and maps Tag and TagDto objects
 */
@Component
public class TagMapperImpl implements Mapper<TagDto, Tag> {

    private ModelMapper modelMapper;

    /**
     * We want to map a TagDto object to a Tag
     * @param tag
     * @return the mapped Tag object
     */
    @Override
    public TagDto mapTo(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    /**
     * We want to map a TagDto object to a Tag
     * @param tagDto
     * @return the mapped Tag object
     */
    @Override
    public Tag mapFrom(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

}
