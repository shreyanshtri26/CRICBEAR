package com.example.tournament.controller;

import com.example.tournament.model.BowlingScore;
import com.example.tournament.service.BowlingScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BowlingScoreControllerTest {

    @InjectMocks
    private BowlingScoreController bowlingScoreController;

    @Mock
    private BowlingScoreService bowlingScoreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBallByBall_Success() {
        // Arrange
        Integer mid = 1;
        Map<String, List<BowlingScore>> mockBowlingScoresData = new HashMap<>();
        mockBowlingScoresData.put("team1", Collections.singletonList(new BowlingScore("Player1", 10L, 3, 2, 1)));

        when(bowlingScoreService.getAllDataByMid(mid)).thenReturn(mockBowlingScoresData);

        // Act
        ResponseEntity<?> response = bowlingScoreController.getBallByBall(mid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBowlingScoresData, response.getBody());
    }

    @Test
    public void testGetBallByBall_NoDataFound() {
        // Arrange
        Integer mid = 2;
        when(bowlingScoreService.getAllDataByMid(mid)).thenReturn(Collections.emptyMap());

        // Act
        ResponseEntity<?> response = bowlingScoreController.getBallByBall(mid);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No data found for the provided match ID", response.getBody());
    }

    @Test
    public void testGetBallByBall_Exception() {
        // Arrange
        Integer mid = 3;
        when(bowlingScoreService.getAllDataByMid(mid)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = bowlingScoreController.getBallByBall(mid);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while retrieving data", response.getBody());
    }
}
