package com.myflashcardsapi.flashcards_api.services.impl;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.Tag;
import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.domain.dto.DeckDto;
import com.myflashcardsapi.flashcards_api.domain.dto.TagDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.TagMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.TagRepository;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.TagService;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;

    private UserRepository userRepository;

    private TagMapperImpl tagMapper;

    public TagServiceImpl(TagRepository tagRepository, UserRepository userRepository, TagMapperImpl tagMapper) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagDto createTag(Long userId, TagDto tagDto) throws BadRequestException {
        User user = userRepository.findById(userId).get();
        if(tagRepository.existsByNameIgnoreCaseAndUserId(tagDto.getName(), userId)) {
            throw new BadRequestException("Tag with name " + tagDto.getName() + " already exists");
        }

        Tag tag = tagMapper.mapFrom(tagDto);
        tag.setUser(user);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.mapTo(savedTag);
    }

    @Override
    public TagDto updateTag(Long userId, Long tagId, TagDto tagDto) throws BadRequestException {
        Tag existingTag = tagRepository.findByIdAndUserId(tagId, userId).get();
        if (!existingTag.getName().equalsIgnoreCase(tagDto.getName())) {
            if(tagRepository.existsByNameIgnoreCaseAndUserId(tagDto.getName(), userId)) {
                throw new BadRequestException("Tag with the name " + tagDto.getName() + " already exists");
            }
            existingTag.setName(tagDto.getName());
        }

        Tag updatedTag = tagRepository.save(existingTag);
        return tagMapper.mapTo(updatedTag);
    }

    @Override
    public void deleteTag(Long userId, Long tagId) {
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId).get();
        tagRepository.delete(tag);
    }

    @Override
    public List<TagDto> getAllTagsForUser(Long userId) {
        userRepository.findById(userId).get();
        List<Tag> tagList = tagRepository.findByUserId(userId);
        List<TagDto> tagDtoList = new ArrayList<>();
        for(Tag tag : tagList) {
            tagDtoList.add(tagMapper.mapTo(tag));
        }
        return tagDtoList;
    }

    @Override
    public Optional<TagDto> getTagByIdAndUser(Long tagId, Long userId) {
        return tagRepository.findByIdAndUserId(tagId, userId).map(tagMapper::mapTo);
    }
}
