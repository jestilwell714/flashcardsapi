package com.myflashcardsapi.flashcards_api.mappers;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;

public interface Mapper <X, Y>{

    X mapTo(Y y);

    Y mapFrom(X x);
}
