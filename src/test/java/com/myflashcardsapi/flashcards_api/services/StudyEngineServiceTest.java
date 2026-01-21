package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.services.impl.StudyEngineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class StudyEngineServiceTest {

    private StudyEngineService studyEngineService;

    @BeforeEach
    public void setUp() {

        studyEngineService = new StudyEngineServiceImpl();
    }

    @Test
    public void testCramProbability() {
        FlashCardDto card1 = new FlashCardDto();
        card1.setId(1L);
        card1.setWeight(100.0);

        FlashCardDto card2 = new FlashCardDto();
        card2.setId(2L);
        card2.setWeight(150.0);

        FlashCardDto card3 = new FlashCardDto();
        card3.setId(3L);
        card3.setWeight(200.0);

        FlashCardDto card4 = new FlashCardDto();
        card4.setId(4L);
        card4.setWeight(300.0);

        FlashCardDto card5 = new FlashCardDto();
        card5.setId(5L);
        card5.setWeight(400.0);

        FlashCardDto card6 = new FlashCardDto();
        card6.setId(6L);
        card6.setWeight(500.0);


        List<FlashCardDto> deck = List.of(card1, card2, card3, card4, card5, card6);

        int[] counts = new int[6];
        int iterations = 10000;

        for (int i = 0; i < iterations; i++) {
            List<FlashCardDto> batch = studyEngineService.getCardBatchForCramMode(deck);
            for (FlashCardDto picked : batch) {
                counts[picked.getId().intValue() - 1]++;
            }
        }

        assertTrue(counts[0] > counts[1] && counts[1] > counts[2] && counts[2] > counts[3] && counts[3] > counts[4] && counts[4] > counts[5]);
        assertTrue(counts[0] > 0 && counts[1] > 0 && counts[2] > 0 && counts[3] > 0 && counts[4] > 0 && counts[5] > 0);
    }
}
