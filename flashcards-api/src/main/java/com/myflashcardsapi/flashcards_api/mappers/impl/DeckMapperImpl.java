package com.myflashcardsapi.flashcards_api.mappers.impl;

import com.myflashcardsapi.flashcards_api.domain.Deck;
import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.dto.DeckDto;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Implementation class that implements Mapper and maps Deck and DeckDto objects
 */
@Component
public class DeckMapperImpl implements Mapper<DeckDto, Deck> {
    private ModelMapper modelMapper;

    /**
     * We want to map a Deck object to a DeckDto
     * @param deck
     * @return the mapped DeckDto object
     */
    @Override
    public DeckDto mapTo(Deck deck) {
        return modelMapper.map(deck, DeckDto.class);
    }

    /**
     * We want to map a DeckDto object to a Deck
     * @param deckDto
     * @return the mapped Deck object
     */
    @Override
    public Deck mapFrom(DeckDto deckDto) {return modelMapper.map(deckDto, Deck.class); }

}
