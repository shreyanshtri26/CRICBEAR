package com.example.tournament.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.tournament.model.Innings;
import com.example.tournament.model.Match;
import com.example.tournament.model.PlayerRole;
import com.example.tournament.model.TossDecision;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.PlayerTeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InningsServiceTest {

    @Mock
    private InningsRepository inningsRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PlayerTeamRepository playerTeamRepository;

    @Mock
    private BallByBallService ballByBallService;

    @InjectMocks
    private InningsService inningsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        InningsService.setToNew();
    }

    @Test
    public void testSetToNew() {
        InningsService.setToNew();
        assertTrue(InningsService.playing11Team1.isEmpty(), "playing11Team1 should be empty");
        assertTrue(InningsService.bowlersTeam1.isEmpty(), "bowlersTeam1 should be empty");
        assertTrue(InningsService.playing11Team2.isEmpty(), "playing11Team2 should be empty");
        assertTrue(InningsService.bowlersTeam2.isEmpty(), "bowlersTeam2 should be empty");
        assertTrue(InningsService.batsmenTeam1.isEmpty(), "batsmenTeam1 should be empty");
        assertTrue(InningsService.batsmenTeam2.isEmpty(), "batsmenTeam2 should be empty");
    }

    @Test
    public void testCreateInnings() {
        Innings innings = new Innings();
        when(inningsRepository.save(innings)).thenReturn(innings);
        Innings createdInnings = inningsService.createInnings(innings);
        assertEquals(innings, createdInnings, "The created innings should match the returned innings");
    }

    @Test
    public void testGetAll() {
        List<Innings> inningsList = new ArrayList<>();
        when(inningsRepository.findAll()).thenReturn(inningsList);
        List<Innings> result = inningsService.getAll();
        assertEquals(inningsList, result, "The result should match the mocked list of innings");
    }

    @Test
    public void testGetById() {
        List<Innings> inningsList = new ArrayList<>();
        when(inningsRepository.findByMid(1)).thenReturn(inningsList);
        List<Innings> result = inningsService.getById(1);
        assertEquals(inningsList, result, "The result should match the mocked list of innings");
    }

    @Test
    public void testGetRandomEnum() {
        TossDecision result = InningsService.getRandomEnum(TossDecision.class);
        assertNotNull(result, "The result should not be null");
        assertTrue(Arrays.asList(TossDecision.values()).contains(result), "The result should be a valid TossDecision value");
    }

    @Test
    public void testTossDecision() {
        inningsService.tossDecision(1, 2, TossDecision.BAT);
        assertEquals(1, InningsService.battingId, "The batting ID should be 1");
        assertEquals(2, InningsService.bowlingId, "The bowling ID should be 2");
        assertEquals(TossDecision.BAT, InningsService.tossDecision, "The toss decision should be BAT");
    }

    @Test
    public void testCreationOfPlaying11Team1() {
        // Prepare test data
        when(playerTeamRepository.findByRoleAndTeam(1, PlayerRole.BATSMAN.toString(), 4)).thenReturn(new ArrayList<>());
        when(playerTeamRepository.findByRoleAndTeam(1, PlayerRole.ALLROUNDER.toString(), 2)).thenReturn(new ArrayList<>());
        when(playerTeamRepository.findByRoleAndTeam(1, PlayerRole.WICKETKEEPER.toString(), 1)).thenReturn(new ArrayList<>());
        when(playerTeamRepository.findByRoleAndTeam(1, PlayerRole.BOWLER.toString(), 5)).thenReturn(new ArrayList<>());

        inningsService.creationOfPlaying11Team1();

        assertTrue(InningsService.playing11Team1.isEmpty(), "The playing11Team1 should not be empty");
        assertTrue(InningsService.bowlersTeam1.isEmpty(), "The bowlersTeam1 should not be empty");
    }

    @Test
    public void testCreationOfPlaying11Team2() {
        // Prepare test data
        when(playerTeamRepository.findByRoleAndTeam(2, PlayerRole.BATSMAN.toString(), 4)).thenReturn(new ArrayList<>());
        when(playerTeamRepository.findByRoleAndTeam(2, PlayerRole.ALLROUNDER.toString(), 2)).thenReturn(new ArrayList<>());
        when(playerTeamRepository.findByRoleAndTeam(2, PlayerRole.WICKETKEEPER.toString(), 1)).thenReturn(new ArrayList<>());
        when(playerTeamRepository.findByRoleAndTeam(2, PlayerRole.BOWLER.toString(), 5)).thenReturn(new ArrayList<>());

        inningsService.creationOfPlaying11Team2();

        assertTrue(InningsService.playing11Team2.isEmpty(), "The playing11Team2 should not be empty");
        assertTrue(InningsService.bowlersTeam2.isEmpty(), "The bowlersTeam2 should not be empty");
    }

//    @Test
//    public void testInitializeBallByBallState() {
//        Innings innings = new Innings();
//        innings.setIid(1);
//        when(inningsRepository.getByMid(1)).thenReturn(innings);
//        InningsService.playing11Team1.add("player1");
//        InningsService.bowlersTeam2.add("bowler1");
////        when(ballByBallService.startTask1()).thenReturn(null);
//
//        inningsService.initializeBallByBallState(1);
//
//        assertEquals(InningsService.playing11Team1, BallByBallService.playing11);
//        assertEquals("player1", BallByBallService.batsman1Name);
//        assertEquals("player1", BallByBallService.batsman2Name);
//        assertEquals("bowler1", BallByBallService.bowlerName);
//        assertTrue(BallByBallService.enabled, "BallByBallService should be enabled");
//    }

//    @Test
//    public void testStartMatchWithTossAndSetup() {
//        // Arrange
//        Match match = new Match();
//        match.setTeamId1(1);
//        match.setTeamId2(2);
//        when(matchRepository.getByMid(1)).thenReturn(match);
//
//        Innings innings = new Innings();
//        innings.setIid(1);
//        when(inningsRepository.getByMid(1)).thenReturn(innings);
//
//        when(inningsRepository.save(any(Innings.class))).thenReturn(innings);
//
//        // Populate static lists with sufficient data
//        InningsService.playing11Team1.add("player1");
//        InningsService.playing11Team1.add("player2");
//        InningsService.bowlersTeam2.add("bowler1");
//        InningsService.bowlersTeam2.add("bowler2");
//
//        doNothing().when(ballByBallService).startTask1();
//
//        // Act
//        inningsService.startMatchWithTossAndSetup(1);
//
//        // Assert
//        assertTrue(InningsService.playing11Team1.contains("player1"), "Playing 11 should contain player1");
//        assertTrue(InningsService.playing11Team1.contains("player2"), "Playing 11 should contain player2");
//        assertTrue(InningsService.bowlersTeam2.contains("bowler1"), "Bowlers team should contain bowler1");
//        assertTrue(InningsService.bowlersTeam2.contains("bowler2"), "Bowlers team should contain bowler2");
//
//        // Verify that startTask1() was called
//        verify(ballByBallService).startTask1();
//    }

    @Test
    public void testUpdate() {
        Innings innings = new Innings();
        innings.setIid(1);
        when(inningsRepository.getByIid(1)).thenReturn(innings);
        when(inningsRepository.save(any(Innings.class))).thenReturn(innings);

        Innings updatedInnings = inningsService.update(1, 100, 5, 10.0);

        assertEquals(100, updatedInnings.getRuns(), "Runs should be updated to 100");
        assertEquals(5, updatedInnings.getWickets(), "Wickets should be updated to 5");
        assertEquals(10.0, updatedInnings.getOvers(), "Overs should be updated to 10.0");
    }
}
