package com.myflashcardsapi.flashcards_api.services;
import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.Tag;
import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.domain.dto.TagDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.TagMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.TagRepository;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.impl.DeckServiceImpl;
import com.myflashcardsapi.flashcards_api.services.impl.TagServiceImpl;
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
public class TagServiceTest {
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private TagRepository mockTagRepository;

    @Mock
    private TagMapperImpl mockTagMapper;

    private User user;
    private Tag tag;
    private TagDto tagDto;
    private FlashCard flashcard1;
    private FlashCard flashcard2;
    private FlashCard flashcard4;

    @InjectMocks
    private TagServiceImpl tagService;

    private TestEntityBuilderUnit testEntityBuilder;

    @BeforeEach
    void setUp() {
        testEntityBuilder = new TestEntityBuilderUnit();
        testEntityBuilder.testEntitySetUp();
        tag = testEntityBuilder.getDataStructureTag();
        user = testEntityBuilder.getUser();
        flashcard1 = testEntityBuilder.getFlashCard1();
        flashcard2 = testEntityBuilder.getFlashCard2();
        flashcard4 = testEntityBuilder.getFlashCard4();

        tagDto = new TagDto(tag.getId(),tag.getName(),tag.getUser().getId(),null);
        tagDto.setFlashCardIds(List.of(flashcard1.getId(),flashcard2.getId(), flashcard4.getId()));
    }

    @Test
    void givenTagDtoWhenCreateTagReturnTagDto() throws BadRequestException {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockTagRepository.existsByNameIgnoreCaseAndUserId(tagDto.getName(), user.getId())).thenReturn(false);
        when(mockTagMapper.mapFrom(tagDto)).thenReturn(tag);
        when(mockTagRepository.save(tag)).thenReturn(tag);
        when(mockTagMapper.mapTo(tag)).thenReturn(tagDto);

        TagDto createdTagDto = tagService.createTag(user.getId(), tagDto);
        assert(createdTagDto.getName()).equals(tagDto.getName());
        assert(!createdTagDto.equals(null));
        verify(mockTagRepository).save(tag);
    }

    @Test
    void givenTagDtoWhenCreateTagWithSameNameThrowException() throws BadRequestException {
        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mockTagRepository.existsByNameIgnoreCaseAndUserId(tagDto.getName(), user.getId())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> tagService.createTag(user.getId(), tagDto));
    }

    @Test
    void givenTagDtoWhenUpdateTagReturnTagDto() throws BadRequestException {
        tagDto.setName("Operating systems");
        when(mockTagRepository.findByIdAndUserId(tag.getId(), user.getId())).thenReturn(Optional.of(tag));
        when(mockTagRepository.existsByNameIgnoreCaseAndUserId(tagDto.getName(), user.getId())).thenReturn(false);
        when(mockTagRepository.save(tag)).thenReturn(tag);
        when(mockTagMapper.mapTo(tag)).thenReturn(tagDto);
        TagDto updatedTagDto = tagService.updateTag(user.getId(),tag.getId(),tagDto);

        assertThat(updatedTagDto).isNotNull();
        assert(tag.getName().equals("Operating systems"));
        verify(mockTagRepository).save(tag);
    }

    @Test
    void givenTagDtoWithSameNameWhenUpdateTagThrowException() throws BadRequestException {
        tagDto.setName("Algorithm Tag");
        when(mockTagRepository.findByIdAndUserId(tag.getId(), user.getId())).thenReturn(Optional.of(tag));
        when(mockTagRepository.existsByNameIgnoreCaseAndUserId(tagDto.getName(), user.getId())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> tagService.updateTag(user.getId(),tag.getId(), tagDto));
    }


}
