package com.example.tournament.service;

import com.example.tournament.model.BowlingScore;
import com.example.tournament.model.Innings;
import com.example.tournament.model.Player;
import com.example.tournament.repository.BowlingScoreRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BowlingScoreServiceTest {

    @Mock
    private BowlingScoreRepository bowlingScoreRepository;

    @Mock
    private PlayerTeamService playerTeamService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private InningsRepository inningsRepository;

    @InjectMocks
    private BowlingScoreService bowlingScoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<BowlingScore> bowlingScores = Arrays.asList(
                new BowlingScore("Bowler1", 10L, 5, 2, 1),
                new BowlingScore("Bowler2", 20L, 8, 3, 1)
        );
        when(bowlingScoreRepository.findAll()).thenReturn(bowlingScores);
        List<BowlingScore> result = bowlingScoreService.getAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(score -> score.getPlayerName().equals("Bowler1")));
    }

    @Test
    void testCreate() {
        List<String> bowlerNames = Arrays.asList("Bowler1", "Bowler2");
        Integer inningsId = 1;
        when(bowlingScoreRepository.save(any(BowlingScore.class))).thenReturn(new BowlingScore());
        boolean result = bowlingScoreService.create(bowlerNames, inningsId);
        assertTrue(result);
        verify(bowlingScoreRepository, times(2)).save(any(BowlingScore.class));
    }

    @Test
    void testUpdate() {
        String bowlerId = "Bowler1";
        Long value = 10L;
        int overs = 5;
        int wickets = 2;
        Integer inningsId = 1;
        BowlingScore existingScore = new BowlingScore(bowlerId, 5L, 3, 1, inningsId);
        when(bowlingScoreRepository.findByNameAndIid(bowlerId, inningsId)).thenReturn(existingScore);
        bowlingScoreService.update(bowlerId, value, overs, wickets, inningsId);
        verify(bowlingScoreRepository).save(argThat(score ->
                score.getOvers() == 5 &&
                        score.getRunsGiven() == 15L &&
                        score.getWickets() == 3
        ));
    }

    @Test
    void testUpdatePlayerStatsForBowling() {
        String bowlerId = "Bowler1";
        int value = 10;
        int overs = 5;
        int wickets = 2;
        Player player = new Player();
        player.setPid(1);
        when(playerRepository.findByName(bowlerId)).thenReturn(player);
        bowlingScoreService.updatePlayerStatsForBowling(bowlerId, value, overs, wickets);
        verify(playerTeamService).updatePlayerTeam(0, 1, wickets, overs, value, player.getPid());
    }

    @Test
    void testGetAllDataByMid() {
        int matchId = 1;
        Innings innings1 = new Innings();
        innings1.setIid(1);
        Innings innings2 = new Innings();
        innings2.setIid(2);
        List<Innings> inningsList = Arrays.asList(innings1, innings2);
        when(inningsRepository.findByMid(matchId)).thenReturn(inningsList);
        List<BowlingScore> bowlingScores1 = Arrays.asList(new BowlingScore("Bowler1", 10L, 5, 2, 1));
        List<BowlingScore> bowlingScores2 = Arrays.asList(new BowlingScore("Bowler2", 20L, 8, 3, 1));
        when(bowlingScoreRepository.findByIid(1)).thenReturn(bowlingScores1);
        when(bowlingScoreRepository.findByIid(2)).thenReturn(bowlingScores2);
        Map<String, List<BowlingScore>> result = bowlingScoreService.getAllDataByMid(matchId);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("Innings1"));
        assertTrue(result.containsKey("Innings2"));
        assertEquals(bowlingScores1, result.get("Innings1"));
        assertEquals(bowlingScores2, result.get("Innings2"));
    }
}