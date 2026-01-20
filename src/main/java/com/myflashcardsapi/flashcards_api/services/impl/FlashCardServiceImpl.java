package com.myflashcardsapi.flashcards_api.services.impl;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.Folder;
import com.myflashcardsapi.flashcards_api.domain.Tag;
import com.myflashcardsapi.flashcards_api.domain.dto.DeckDto;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.FlashCardMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.DeckRepository;
import com.myflashcardsapi.flashcards_api.repositories.FlashCardRepository;
import com.myflashcardsapi.flashcards_api.repositories.TagRepository;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.*;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlashCardServiceImpl implements FlashCardService {

    private FlashCardRepository flashCardRepository;

    private DeckService deckService;

    private TagRepository tagRepository;

    private UserRepository userRepository;

    private FolderService folderService;

    private FlashCardMapperImpl flashCardMapper;

    private StudyEngineService studyEngineService;

    public FlashCardServiceImpl(FlashCardRepository flashCardRepository, DeckService deckService, TagRepository tagRepository, UserRepository userRepository, FolderService folderService, FlashCardMapperImpl flashCardMapper, StudyEngineService studyEngineService) {
        this.flashCardRepository = flashCardRepository;
        this.deckService = deckService;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.folderService = folderService;
        this.flashCardMapper = flashCardMapper;
        this.studyEngineService = studyEngineService;
    }

    @Override
    public FlashCardDto createFlashCard(Long userId, Long deckId, FlashCardDto flashCardDto) throws BadRequestException {
        // Set deck
        FlashCard flashCard = flashCardMapper.mapFrom(flashCardDto);
        flashCard.setDeck(deckService.getDeckByIdAndUser(deckId, userId).get());

        // Set or create tags
        if(flashCardDto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findByIdInAndUserId(flashCardDto.getTagIds(), userId);
            if (tags.size() != flashCardDto.getTagIds().size()) {
                throw new BadRequestException("One or more provided tag IDs are invalid or not owned by user.");
            }
            flashCard.setTags(tags);
        }

        FlashCard savedFlashCard = flashCardRepository.save(flashCard);
        return flashCardMapper.mapTo(savedFlashCard);
    }

    @Override
    public FlashCardDto updateFlashCard(Long userId, Long flashCardId, FlashCardDto flashCardDto) throws BadRequestException {
        FlashCard flashCard = flashCardRepository.findByIdAndDeckUserId(flashCardId, userId).get();
        
        // question and answer
        flashCard.setQuestion(flashCardDto.getQuestion());
        flashCard.setAnswer(flashCardDto.getAnswer());

        // Change deck if it has been moved
        Long deckId = flashCard.getDeck().getId();
        if(flashCardDto.getDeckId() != deckId) {
            Deck deck = deckService.getDeckByIdAndUser(flashCardDto.getDeckId(), userId).get();
            flashCard.setDeck(deck);
        }

        // Set or create tags
        if(flashCardDto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findByIdInAndUserId(flashCardDto.getTagIds(), userId);
            if (tags.size() != flashCardDto.getTagIds().size()) {
                throw new BadRequestException("One or more provided tag IDs are invalid or not owned by user.");
            }
            flashCard.setTags(tags);
        }


        FlashCard savedFlashCard = flashCardRepository.save(flashCard);
        return flashCardMapper.mapTo(savedFlashCard);
    }

    @Override
    public void deleteFlashCard(Long userId, Long flashCardId) {
        FlashCard flashCard = flashCardRepository.findByIdAndDeckUserId(flashCardId, userId).get();
        flashCardRepository.delete(flashCard);
    }

    @Override
    public Optional<FlashCardDto> getFlashCardByIdAndUser(Long flashcardId, Long userId) {
        return flashCardRepository.findByIdAndDeckUserId(flashcardId, userId).map(flashCardMapper::mapTo);
    }

    @Override
    public List<FlashCardDto> getFlashCardsByDeckIdAndUser(Long deckId, Long userId) {
        deckService.getDeckByIdAndUser(deckId, userId).get();
        List<FlashCard> flashCards = flashCardRepository.findByDeckIdAndDeckUserId(deckId, userId);
        List<FlashCardDto> flashCardDtoList = new ArrayList<>();
        for(FlashCard flashCard : flashCards) {
            flashCardDtoList.add(flashCardMapper.mapTo(flashCard));
        }
        return flashCardDtoList;
    }

    @Override
    public List<FlashCardDto> getAllFlashCardsForUser(Long userId) {
        userRepository.findById(userId).get();
        List<FlashCard> flashCards = flashCardRepository.findByDeckUserId(userId);
        List<FlashCardDto> flashCardDtoList = new ArrayList<>();
        for(FlashCard flashCard : flashCards) {
            flashCardDtoList.add(flashCardMapper.mapTo(flashCard));
        }
        return flashCardDtoList;
    }

    @Override
    public List<FlashCardDto> getFlashCardsByTagsIdAndUser(List<Long> tagIds, Long userId) throws BadRequestException {
        List<Tag> tags = tagRepository.findByIdInAndUserId(tagIds, userId);
        if (tags.size() != tagIds.size()) {
            throw new BadRequestException("One or more provided tag IDs are invalid or not owned by user.");
        }
        List<FlashCard> flashCards = flashCardRepository.findByTagsIdInAndDeckUserId(tagIds, userId);
        List<FlashCardDto> flashCardDtoList = new ArrayList<>();
        for(FlashCard flashCard : flashCards) {
            flashCardDtoList.add(flashCardMapper.mapTo(flashCard));
        }
        return flashCardDtoList;
    }

    @Override
    public List<FlashCardDto> getFlashCardsInFolder(Long folderId, Long userId) {
        List<Long> folderIds = folderService.findAllDescendantFolderIds(folderId, userId);
        List<FlashCard> flashCards = new ArrayList<>();
        for(Long id: folderIds) {
            flashCards.addAll(flashCardRepository.findByDeckFolderIdAndDeckUserId(id, userId));
        }
        List<FlashCardDto> flashCardDtoList = new ArrayList<>();
        for(FlashCard flashCard : flashCards) {
            flashCardDtoList.add(flashCardMapper.mapTo(flashCard));
        }
        return flashCardDtoList;
    }

    @Override
    public List<FlashCardDto> getFlashCardsForCramByDeckId(Long userId, Long deckId) {
        List<FlashCardDto> flashCardDtoList = getFlashCardsByDeckIdAndUser(userId,deckId);
        return studyEngineService.getCardBatchForCramMode(flashCardDtoList);
    }

    @Override
    public List<FlashCardDto> getFlashCardsForCramByFolderId(Long userId, Long folderId) {
        List<FlashCardDto> flashCardDtoList = getFlashCardsInFolder(folderId,userId);
        return studyEngineService.getCardBatchForCramMode(flashCardDtoList);
    }

    @Override
    public List<FlashCardDto> getFlashCardsForCramByTagsId(Long userId, List<Long> tagsId) throws BadRequestException {
        List<FlashCardDto> flashCardDtoList = getFlashCardsByTagsIdAndUser(tagsId,userId);
        return studyEngineService.getCardBatchForCramMode(flashCardDtoList);
    }

}
