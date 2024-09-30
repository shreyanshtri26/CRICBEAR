package com.example.tournament.controller;

import com.example.tournament.model.DTO.MatchResultDTO;
import com.example.tournament.service.MatchResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MatchResultControllerTest {

    @InjectMocks
    private MatchResultController matchResultController;

    @Mock
    private MatchResultService matchResultService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetByMatchId() {
        Integer matchId = 1;
        MatchResultDTO matchResultDTO = new MatchResultDTO();
        when(matchResultService.getResultByMatchId(matchId)).thenReturn(matchResultDTO);

        ResponseEntity<?> response = matchResultController.getByMatchId(matchId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(matchResultDTO, response.getBody());
        verify(matchResultService, times(1)).getResultByMatchId(matchId);
    }
}