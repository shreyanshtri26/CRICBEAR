package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private RegTeamService regTeamService;

    @Mock
    private TournamentService tournamentService;

    @Mock
    private InningsService inningsService;

    @Mock
    private MatchResultService matchResultService;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private MatchService matchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Match> matches = new ArrayList<>();
        when(matchRepository.findAll()).thenReturn(matches);

        // Act
        List<Match> result = matchService.findAll();

        // Assert
        assertEquals(matches, result);
        verify(matchRepository).findAll();
    }

    @Test
    public void testFindById() {
        // Arrange
        int matchId = 1;
        Match match = new Match();
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // Act
        Match result = matchService.findById(matchId);

        // Assert
        assertEquals(match, result);
        verify(matchRepository).findById(matchId);
    }

    @Test
    public void testSave() {
        // Arrange
        Match match = new Match();
        when(matchRepository.save(match)).thenReturn(match);

        // Act
        Match result = matchService.save(match);

        // Assert
        assertEquals(match, result);
        verify(matchRepository).save(match);
    }

    @Test
    public void testDelete() {
        // Arrange
        Match match = new Match();

        // Act
        matchService.delete(match);

        // Assert
        verify(matchRepository).delete(match);
    }



    @Test
    public void testFindByMatchType() {
        // Arrange
        MatchType matchType = MatchType.NORMAL;
        List<Match> matches = new ArrayList<>();
        when(matchRepository.findByMatchType(matchType)).thenReturn(matches);

        // Act
        List<Match> result = matchService.findByMatchType(matchType);

        // Assert
        assertEquals(matches, result);
        verify(matchRepository).findByMatchType(matchType);
    }

    @Test
    public void testStartMatch_Success() throws InterruptedException {
        // Arrange
        int mid = 1;
        Match match = new Match();
        match.setStatus(MatchStatus.UPCOMING);
        match.setMatchDate(LocalDateTime.now().toLocalDate().atTime(18, 0));
        when(matchRepository.getByMid(mid)).thenReturn(match);
        when(matchRepository.save(match)).thenReturn(match);

        // Act
        String result = matchService.startMatch(mid);

        // Assert
        assertEquals("Match started", result);
        assertEquals(MatchStatus.LIVE, match.getStatus());
        verify(matchRepository).getByMid(mid);
        verify(matchRepository).save(match);
        verify(inningsService).startMatchWithTossAndSetup(mid);
    }

    @Test
    public void testStartMatch_Failure() throws InterruptedException {
        // Arrange
        int mid = 1;
        Match match = new Match();
        match.setStatus(MatchStatus.UPCOMING);
        match.setMatchDate(LocalDateTime.now().plusDays(1).toLocalDate().atTime(18, 0));
        when(matchRepository.getByMid(mid)).thenReturn(match);

        // Act
        String result = matchService.startMatch(mid);

        // Assert
        assertNull(result);
        verify(matchRepository).getByMid(mid);
        verify(matchRepository, never()).save(match);
        verify(inningsService, never()).startMatchWithTossAndSetup(mid);
    }


    @Test
    public void testFinalSchedule() {
        // Arrange
        int tid = 1;
        int uid = 1;
        Tournament tournament = new Tournament();
        tournament.setUid(uid);
        tournament.setStartDate(LocalDateTime.now().plusDays(1));

        List<Match> semiFinalMatches = new ArrayList<>();
        Match semiFinalMatch1 = new Match();
        semiFinalMatch1.setMid(1);
        Match semiFinalMatch2 = new Match();
        semiFinalMatch2.setMid(2);
        semiFinalMatches.add(semiFinalMatch1);
        semiFinalMatches.add(semiFinalMatch2);

        MatchResult matchResult1 = new MatchResult();
        matchResult1.setWinnerId(1);
        MatchResult matchResult2 = new MatchResult();
        matchResult2.setWinnerId(2);

        when(tournamentService.getByTid(tid)).thenReturn(tournament);
        when(matchRepository.getByTidAndMatchType(tid, MatchType.SEMIFINAL)).thenReturn(semiFinalMatches);
        when(matchResultService.getMatchResultByMatchId(1)).thenReturn(matchResult1);
        when(matchResultService.getMatchResultByMatchId(2)).thenReturn(matchResult2);

        // Act
        Match result = matchService.finalSchedule(tid, uid);

        // Assert
        assertNotNull(result);
        assertEquals(MatchType.FINAL, result.getMatchType());
        verify(tournamentService).getByTid(tid);
        verify(matchRepository).getByTidAndMatchType(tid, MatchType.SEMIFINAL);
        verify(matchResultService).getMatchResultByMatchId(1);
        verify(matchResultService).getMatchResultByMatchId(2);
    }

    @Test
    public void testUpdateMatch_IfEligible_Success() {
        // Arrange
        int mid = 1;
        Match match = new Match();
        match.setMatchType(MatchType.SEMIFINAL);
        match.setMatchDate(LocalDateTime.now().plusDays(3));
        Match existingMatch = new Match();
        existingMatch.setMatchDate(LocalDateTime.now().plusDays(1));
        when(matchRepository.getByMid(mid)).thenReturn(existingMatch);
        when(matchRepository.save(match)).thenReturn(match);

        // Act
        Match result = matchService.updateMatchIfEligible(mid, match);

        // Assert
        assertNotNull(result);
        assertEquals(match, result);
        verify(matchRepository).getByMid(mid);
        verify(matchRepository).save(match);
    }


}
