package com.example.tournament.controller;

import com.example.tournament.model.DTO.InningsDTO;
import com.example.tournament.service.BallByBallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BallByBallControllerTest {

    @InjectMocks
    private BallByBallController ballByBallController;

    @Mock
    private BallByBallService ballByBallService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBallByBallSuccess() {
        Integer matchId = 1;
        InningsDTO inningsDTO = new InningsDTO();
        Map<String, InningsDTO> ballByBallData = Map.of("Innings1", inningsDTO);

        when(ballByBallService.getBallByBall(matchId)).thenReturn(ballByBallData);

        ResponseEntity<?> response = ballByBallController.getBallByBall(matchId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ballByBallData, response.getBody());
    }

    @Test
    public void testGetBallByBallNotFound() {
        Integer matchId = 1;

        when(ballByBallService.getBallByBall(matchId)).thenReturn(null);

        ResponseEntity<?> response = ballByBallController.getBallByBall(matchId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No data found for the provided match ID", response.getBody());
    }

    @Test
    public void testGetBallByBallError() {
        Integer matchId = 1;

        when(ballByBallService.getBallByBall(matchId)).thenThrow(new RuntimeException());

        ResponseEntity<?> response = ballByBallController.getBallByBall(matchId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while retrieving data", response.getBody());
    }
}