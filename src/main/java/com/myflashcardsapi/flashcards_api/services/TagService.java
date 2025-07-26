package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Tag createTag(Long userId, Long flashCardId, Tag tag);

    Tag updateTag(Long userId, Tag tag);

    Tag deleteTag(Long userId, Tag tag);

    List<Tag> getAllTagsForUser(Long userId);

    Optional<Tag> getTagsByIdAndUser(Long tagId, Long userId);

}
