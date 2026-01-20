package com.myflashcardsapi.flashcards_api.services.impl;

import com.myflashcardsapi.flashcards_api.domain.FlashCard;
import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.services.StudyEngineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class StudyEngineServiceImpl implements StudyEngineService {
    private final int BATCHNUM = 5;

    @Override
    public List<FlashCardDto> getCardBatchForCramMode(List<FlashCardDto> flashCardDtoList) {
        if(flashCardDtoList == null || flashCardDtoList.isEmpty()) return new ArrayList<>();

        double capAndBuffer = 600.0;

        Random rand = new Random();
        List<FlashCardDto> batch = new ArrayList<>();
        List<FlashCardDto> cards = new ArrayList<>(flashCardDtoList);


        int size = Math.min(flashCardDtoList.size(),BATCHNUM);
        double totalWeight = 0;

        for(FlashCardDto card: cards) {
            totalWeight += capAndBuffer - card.getWeight();
        }

        while(batch.size()< size) {
            double current = 0;
            double target = rand.nextDouble() * totalWeight;

            for(FlashCardDto card : cards) {
                double trueWeight = (capAndBuffer - card.getWeight());
                current += trueWeight;
                if(current >= target ) {
                    batch.add(card);
                    cards.remove(card);
                    totalWeight -= trueWeight;
                    break;
                }
            }
        }

        return batch;
    }
}
