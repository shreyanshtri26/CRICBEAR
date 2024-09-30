package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.InningsDTO;
import com.example.tournament.repository.BallByBallRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class BallByBallServiceTest {

    @Mock
    private BallByBallRepository ballByBallRepository;

    @Mock
    private InningsRepository inningsRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private MatchResultService matchResultService;

    @Mock
    private BattingScoreService battingScoreService;

    @Mock
    private BowlingScoreService bowlingScoreService;

    @InjectMocks
    private BallByBallService ballByBallService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        BallByBallService.enabled = false;
        BallByBallService.enabled2 = false;
    }

    @Test
    void testCreateBattingScorecard() {
        List<String> playing11 = Arrays.asList("Player1", "Player2");
        ballByBallService.createBattingScorecard(playing11);
        verify(battingScoreService, times(1)).create(playing11, BallByBallService.iid);
    }

    @Test
    void testCreateBowlingScorecard() {
        List<String> bowlers = Arrays.asList("Bowler1", "Bowler2");
        ballByBallService.createBowlingScorecard(bowlers);
        verify(bowlingScoreService, times(1)).create(bowlers, BallByBallService.iid);
    }

    @Test
    void testCalculateScoreForFirstinnings() throws InterruptedException {
        BallByBallService.enabled = true;
        ballByBallService.calculateScoreForFirstInnings();
        verify(ballByBallRepository, atLeastOnce()).save(any(BallByBall.class));
    }

    @Test
    void testCalculateScoreOfInnings2() throws InterruptedException {
        BallByBallService.enabled2 = true;
        ballByBallService.calculateScoreOfInnings2();
        verify(ballByBallRepository, atLeastOnce()).save(any(BallByBall.class));
    }

    @Test
    void testGetBallByBall() {
        Innings innings1 = new Innings(1, 1, 1, 1, 100, 5, 10.0);

        // Mock the repository responses
        when(inningsRepository.findByMid(anyInt())).thenReturn(Collections.singletonList(innings1));
        when(teamService.getById(anyInt())).thenReturn(new Team("TeamName", 1, 10L, 5L, 3L, 2L, 0L, 1.5));
        when(ballByBallRepository.findByIid(anyInt())).thenReturn(Collections.emptyList());

        // Call the service method
        Map<String, InningsDTO> result = ballByBallService.getBallByBall(1);

        // Verify the results
        assertTrue(result.containsKey("Innings1"));
    }

    @Test
    void testGetBallByBall_NoInningsFound() {
        // Arrange
        when(inningsRepository.findByMid(anyInt())).thenReturn(Collections.emptyList());

        // Act
        Map<String, InningsDTO> result = ballByBallService.getBallByBall(1);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetBallByBall_Exception() {
        // Arrange
        when(inningsRepository.findByMid(anyInt())).thenThrow(new RuntimeException("Database error"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> ballByBallService.getBallByBall(1));
    }


}