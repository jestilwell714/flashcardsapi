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
            if(deckRepository.existsByNameIgnoreCaseUserIdAndFolderId(deckDto.getName(), userId, folder.getId())) {
                throw new BadRequestException("Deck with the neame " + deckDto.getName() + " already exists in this folder");
            }
        } else if(deckRepository.existsByNameIgnoreCaseUserIdAndFolderId(deckDto.getName(), userId, null)) {
            throw new BadRequestException("Deck with the neame " + deckDto.getName() + " already exists");
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
        if(!deck.getName().equalsIgnoreCase(deckDto.getName())) {
            if (deckRepository.existsByNameIgnoreCaseUserIdAndFolderId(deckDto.getName(), userId, deckDto.getFolderId())) {
                throw new BadRequestException("Deck with the neame " + deckDto.getName() + " already exists in this folder");
            }
            deck.setName(deckDto.getName());
        }
        /**
        // Change folder if moved folder
        if(!(deckDto.getFolderId() == null && deck.getFolder() == null)) {
            if (deckDto.getFolderId() == null && deck.getFolder() != null) {
                deck.setFolder(null);
            } else if (deckDto.getFolderId() != null && deck.getFolder() == null || folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get().getName().equals(deck.getFolder().getName())) {
                if (folderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(deckDto.getName(), userId, folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get().getId())) {
                    throw new BadRequestException("Folder with the name " + folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get().getName() + " already exists in this folder");
                }
                deck.setFolder(folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get());

            }
        }
         */
        // Change folder if moved folder
        if(deck.getFolder() != null) {
            if(!Optional.ofNullable(deckDto.getFolderId()).equals(deck.getFolder().getId())) {
                if (folderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(deckDto.getName(), userId, folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get().getId())) {
                    throw new BadRequestException("Folder with the name " + folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get().getName() + " already exists in this folder");
                }
                deck.setFolder(folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get());
            }
        } else if(deckDto.getFolderId() != null) {
            if (folderRepository.existsByNameIgnoreCaseAndParentFolderIdAndUserId(deckDto.getName(), userId, folderRepository.findByIdAndUserId(null, userId).get().getId())) {
                throw new BadRequestException("Folder with the name " + folderRepository.findByIdAndUserId(deckDto.getFolderId(), userId).get().getName() + " already exists in this folder");
            }
            deck.setFolder(null);
        }
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
