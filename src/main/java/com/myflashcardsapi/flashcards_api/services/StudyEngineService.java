package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;

import java.util.List;

public interface StudyEngineService {
    public List<FlashCardDto> getCramBatch(List<FlashCardDto> flashCardDtoList);
}
