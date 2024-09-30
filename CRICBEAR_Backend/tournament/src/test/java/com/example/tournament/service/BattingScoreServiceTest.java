package com.example.tournament.service;

import com.example.tournament.model.BattingScore;
import com.example.tournament.model.Innings;
import com.example.tournament.model.Player;
import com.example.tournament.repository.BattingScoreRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BattingScoreServiceTest {

    @Mock
    private BattingScoreRepository battingScoreRepository;

    @Mock
    private PlayerTeamService playerTeamService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private InningsRepository inningsRepository;

    @InjectMocks
    private BattingScoreService battingScoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<BattingScore> battingScores = Arrays.asList(
                new BattingScore("Player1", 10L, 5, 2, 1, false, 1),
                new BattingScore("Player2", 20L, 10, 4, 2, true, 1)
        );

        when(battingScoreRepository.findAll()).thenReturn(battingScores);

        List<BattingScore> result = battingScoreService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(score -> score.getPlayerName().equals("Player1")));
    }

    @Test
    void testCreate() {
        List<String> playerNames = Arrays.asList("Player1", "Player2");
        Integer inningsId = 1;

        when(battingScoreRepository.save(any(BattingScore.class))).thenReturn(new BattingScore());

        boolean result = battingScoreService.create(playerNames, inningsId);

        assertTrue(result);
        verify(battingScoreRepository, times(2)).save(any(BattingScore.class));
    }



    @Test
    void testUpdate() throws NoSuchFieldException, IllegalAccessException {
        String batsmanName = "Player1";
        int runsScored = 10;
        int ballsFaced = 5;
        int fours = 2;
        int six = 1;
        boolean isOut = false;
        Integer inningsId = 1;

        BattingScore existingScore = new BattingScore(batsmanName, 5L, 2, 1, 0, false, inningsId);
        when(battingScoreRepository.findByNameAndIid(batsmanName, inningsId)).thenReturn(existingScore);

        battingScoreService.update(batsmanName, runsScored, ballsFaced, fours, six, isOut, inningsId);

        Field field = BattingScore.class.getDeclaredField("isOut");
        field.setAccessible(true); // Allow access to private field

        boolean actualIsOut = (boolean) field.get(existingScore);

        assertEquals(isOut, actualIsOut);
    }


    @Test
    void testUpdatePlayerStatsForBatting() {
        String batsmanName = "Player1";
        int runsScored = 10;
        int ballsFaced = 5;
        int wickets = 0;
        int overs = 0;
        int value = 0;

        Player player = new Player();
        player.setPid(1);
        when(playerRepository.findByName(batsmanName)).thenReturn(player);

        battingScoreService.updatePlayerStatsForBatting(batsmanName, runsScored, ballsFaced, wickets, overs, value);

        verify(playerTeamService).updatePlayerTeam(runsScored, ballsFaced, wickets, overs, value, player.getPid());
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

        List<BattingScore> battingScores1 = Arrays.asList(new BattingScore("Player1", 10L, 5, 2, 1, false, 1));
        List<BattingScore> battingScores2 = Arrays.asList(new BattingScore("Player2", 20L, 10, 4, 2, true, 2));

        when(battingScoreRepository.findByIid(1)).thenReturn(battingScores1);
        when(battingScoreRepository.findByIid(2)).thenReturn(battingScores2);

        Map<String, List<BattingScore>> result = battingScoreService.getAllDataByMid(matchId);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("Innings1"));
        assertTrue(result.containsKey("Innings2"));
        assertEquals(battingScores1, result.get("Innings1"));
        assertEquals(battingScores2, result.get("Innings2"));
    }
}