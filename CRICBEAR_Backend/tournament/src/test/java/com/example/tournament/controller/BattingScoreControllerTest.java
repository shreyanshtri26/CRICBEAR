package com.example.tournament.controller;

import com.example.tournament.model.BattingScore;
import com.example.tournament.service.BattingScoreService;
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
import static org.mockito.Mockito.when;

public class BattingScoreControllerTest {

    @InjectMocks
    private BattingScoreController battingScoreController;

    @Mock
    private BattingScoreService battingScoreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBallByBall_Success() {
        // Arrange
        Integer mid = 1;
        Map<String, List<BattingScore>> mockBattingScoresData = new HashMap<>();
        mockBattingScoresData.put("Innings1", Collections.singletonList(new BattingScore("Player1", 50L, 30, 5, 2, true, 1)));

        when(battingScoreService.getAllDataByMid(mid)).thenReturn(mockBattingScoresData);

        // Act
        ResponseEntity<?> response = battingScoreController.getBallByBall(mid);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBattingScoresData, response.getBody());
    }

    @Test
    public void testGetBallByBall_NoDataFound() {
        // Arrange
        Integer mid = 2;
        when(battingScoreService.getAllDataByMid(mid)).thenReturn(Collections.emptyMap());

        // Act
        ResponseEntity<?> response = battingScoreController.getBallByBall(mid);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No data found for the provided match ID", response.getBody());
    }

    @Test
    public void testGetBallByBall_Exception() {
        // Arrange
        Integer mid = 3;
        when(battingScoreService.getAllDataByMid(mid)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = battingScoreController.getBallByBall(mid);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while retrieving data", response.getBody());
    }
}