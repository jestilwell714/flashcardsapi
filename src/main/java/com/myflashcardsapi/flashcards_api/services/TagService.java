package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.Tag;
import com.myflashcardsapi.flashcards_api.domain.dto.TagDto;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface TagService {
    TagDto createTag(Long userId, TagDto tagDto) throws BadRequestException;

    TagDto updateTag(Long userId, Long tagId, TagDto tagDto) throws BadRequestException;

    void deleteTag(Long userId, Long tagId);

    List<TagDto> getAllTagsForUser(Long userId);

    Optional<TagDto> getTagByIdAndUser(Long tagId, Long userId);
}
