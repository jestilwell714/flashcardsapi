package com.myflashcardsapi.flashcards_api.services.impl;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.Folder;
import com.myflashcardsapi.flashcards_api.domain.User;
import com.myflashcardsapi.flashcards_api.domain.dto.DeckDto;
import com.myflashcardsapi.flashcards_api.mappers.impl.DeckMapperImpl;
import com.myflashcardsapi.flashcards_api.repositories.DeckRepository;
import com.myflashcardsapi.flashcards_api.repositories.FolderRepository;
import com.myflashcardsapi.flashcards_api.repositories.UserRepository;
import com.myflashcardsapi.flashcards_api.services.DeckService;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeckServiceImpl implements DeckService {

    private DeckRepository deckRepository;
    private UserRepository userRepository;
    private FolderRepository folderRepository;

    private DeckMapperImpl deckMapper;

    public DeckServiceImpl(DeckRepository deckRepository, UserRepository userRepository, FolderRepository folderRepository, DeckMapperImpl deckMapper) {
        this.deckRepository = deckRepository;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.deckMapper = deckMapper;
    }

    @Override
    public DeckDto createDeck(Long userId, DeckDto deckDto) throws BadRequestException {
        User user = userRepository.findById(userId).get();

        Folder folder = null;
        if(deckDto.getFolderId() != null) {
            folder = folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get();
            if(deckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), userId, folder.getId())) {
                throw new BadRequestException("Deck with the name " + deckDto.getName() + " already exists in this folder");
            }
        } else if(deckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), userId, null)) {
            throw new BadRequestException("Deck with the name " + deckDto.getName() + " already exists");
        }

        Deck newDeck = deckMapper.mapFrom(deckDto);
        newDeck.setUser(user);
        newDeck.setFolder(folder);

        Deck deck = deckRepository.save(newDeck);
        return deckMapper.mapTo(deck);
    }

    @Override
    public DeckDto updateDeck(Long userId, Long deckId, DeckDto deckDto) throws BadRequestException {
        Deck deck = deckRepository.findByIdAndUserId(deckId, userId).get();

        // Change name if changed
        if(!deck.getName().equalsIgnoreCase(deckDto.getName()) && (Objects.equals(deck.getFolder().getId(), deckDto.getFolderId()))) {
            if (deckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), userId, deckDto.getFolderId())) {
                throw new BadRequestException("Deck with the name " + deckDto.getName() + " already exists in this folder");
            }

        }
        if (!Objects.equals(deckDto.getFolderId(), deck.getFolder().getId())) {
            if (deckDto.getFolderId() == null) {
                // Moving to root
                if (deckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), userId, null)) {
                    throw new BadRequestException("Deck with this name already exists at root");
                }
                deck.setFolder(null);
            } else {
                // Moving to folder
                if (deckRepository.existsByNameIgnoreCaseAndUserIdAndFolderId(deckDto.getName(), userId, deckDto.getFolderId())) {
                    throw new BadRequestException("Deck with this name already exists in this folder");
                }
                Folder newFolder = folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get();
                deck.setFolder(newFolder);
            }
        }
        deck.setName(deckDto.getName());
        Deck updatedDeck = deckRepository.save(deck);
        return deckMapper.mapTo(updatedDeck);
    }

    @Override
    public void deleteDeck(Long userId, Long deckId) {
        Deck deck = deckRepository.findByIdAndUserId(deckId, userId).get();

        deckRepository.delete(deck);
    }

    @Override
    public Optional<Deck> getDeckByIdAndUser(Long deckId, Long userId) {
        return deckRepository.findByIdAndUserId(deckId, userId);
    }

    @Override
    public Optional<DeckDto> getDeckDtoByIdAndUser(Long deckId, Long userId) {
        return deckRepository.findByIdAndUserId(deckId, userId).map(deckMapper::mapTo);
    }

    @Override
    public List<DeckDto> getAllDeckDtosForUser(Long userId) {
        userRepository.findById(userId).get();
        List<Deck> deckList = deckRepository.findByUserId(userId);
        List<DeckDto> deckDtoList = new ArrayList<>();
        for(Deck deck : deckList) {
            deckDtoList.add(deckMapper.mapTo(deck));
        }
        return deckDtoList;
    }


    @Override
    public List<DeckDto> getAllDeckDtosByFolderIdAndUser(Long folderId, Long userId) {
        folderRepository.findByIdAndUserId(folderId, userId).get();

        List<Deck> deckList = deckRepository.findByFolderIdAndUserId(folderId,userId);
        List<DeckDto> deckDtoList = new ArrayList<>();
        for(Deck deck : deckList) {
            deckDtoList.add(deckMapper.mapTo(deck));
        }
        return deckDtoList;
    }
}
