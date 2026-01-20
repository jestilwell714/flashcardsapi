package com.myflashcardsapi.flashcards_api.services;

import com.myflashcardsapi.flashcards_api.domain.dto.FlashCardDto;
import com.myflashcardsapi.flashcards_api.services.impl.StudyEngineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class StudyEngineServiceTest {

    private StudyEngineService studyEngineService;

    @BeforeEach
    public void setUp() {
        // Initialize the implementation manually for a unit test
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

        // Act
        for (int i = 0; i < iterations; i++) {
            List<FlashCardDto> batch = studyEngineService.getCardBatchForCramMode(deck);
            for (FlashCardDto picked : batch) {
                counts[picked.getId().intValue() - 1]++;
            }
        }


        // Assert
        // The hard card should be picked significantly more than the easy card
        assertTrue(counts[0] > counts[1] && counts[1] > counts[2] && counts[2] > counts[3] && counts[3] > counts[4] && counts[4] > counts[5]);
    }
}
