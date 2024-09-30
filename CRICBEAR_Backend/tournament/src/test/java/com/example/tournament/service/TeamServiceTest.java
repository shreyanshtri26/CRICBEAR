package com.example.tournament.service;

import com.example.tournament.model.Match;
import com.example.tournament.model.MatchStatus;
import com.example.tournament.model.Team;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate_Team_Success() {
        Team team = new Team();
        team.setTeamName("Team A");

        when(teamRepository.findByTeamName(team.getTeamName())).thenReturn(null);
        when(teamRepository.save(team)).thenReturn(team);

        Team result = teamService.createTeam(team);
        assertNotNull(result);
        assertEquals(team.getTeamName(), result.getTeamName());
        verify(teamRepository).save(team);
    }

    @Test
    public void testCreate_Team_Failure() {
        Team team = new Team();
        team.setTeamName("Team A");

        when(teamRepository.findByTeamName(team.getTeamName())).thenReturn(team);

        Team result = teamService.createTeam(team);
        assertNull(result);
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    public void testGetById_Found() {
        Team team = new Team();
        team.setTeamId(1);

        when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        Team result = teamService.getById(1);
        assertNotNull(result);
        assertEquals(1, result.getTeamId());
    }

    @Test
    public void testGetById_NotFound() {
        when(teamRepository.findById(1)).thenReturn(Optional.empty());

        Team result = teamService.getById(1);
        assertNull(result);
    }

    @Test
    public void testGetAll() {
        List<Team> teams = new ArrayList<>();
        Team team1 = new Team();
        Team team2 = new Team();
        teams.add(team1);
        teams.add(team2);

        when(teamRepository.findAll()).thenReturn(teams);

        List<Team> result = teamService.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetByTeamName() {
        Team team = new Team();
        team.setTeamName("Team B");

        when(teamRepository.findByTeamName("Team B")).thenReturn(team);

        Team result = teamService.getByTeamName("Team B");
        assertNotNull(result);
        assertEquals("Team B", result.getTeamName());
    }

    @Test
    public void testUpdate_Success() {
        Team existingTeam = new Team();
        existingTeam.setTeamId(1);
        existingTeam.setTeamName("Old Name");

        Team updatedTeam = new Team();
        updatedTeam.setTeamName("New Name");

        when(teamRepository.findById(1)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(existingTeam)).thenReturn(existingTeam);

        Team result = teamService.update(1, updatedTeam);
        assertNotNull(result);
        assertEquals("New Name", result.getTeamName());
    }


    @Test
    public void testDelete() {
        teamService.delete(1);
        verify(teamRepository).deleteById(1);
    }

    @Test
    public void testUpdateAfterResults() {
        Team team = new Team();
        team.setMatchesPlayed(10L);
        team.setMatchesWon(5L);
        team.setMatchesLost(3L);
        team.setMatchesDrawn(2L);
        team.setMatchesAbandoned(1L);
        team.setPoints(15);
        team.setNrr(0.5);

        when(teamRepository.findById(1)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        Team result = teamService.updateAfterResults(team, 2, 1, 1, 0, 0, 2, 0.2);
        assertNotNull(result);
        assertEquals(12, result.getMatchesPlayed());
        assertEquals(6, result.getMatchesWon());
        assertEquals(4, result.getMatchesLost());
        assertEquals(2, result.getMatchesDrawn());
        assertEquals(1, result.getMatchesAbandoned());
        assertEquals(17, result.getPoints());
        assertEquals(0.7, result.getNrr());
    }

    @Test
    public void testUpdateAfterResultsForMatch() {
        Match match = new Match();
        match.setMid(1);
        match.setStatus(MatchStatus.UPCOMING);

        when(matchRepository.getByMid(1)).thenReturn(match);
        when(matchRepository.save(match)).thenReturn(match);

        teamService.updateAfterResultsForMatch(1);
        assertEquals(MatchStatus.COMPLETED, match.getStatus());
        verify(matchRepository).save(match);
    }

    @Test
    public void testFindAllTeamsInDescendingOrder() {
        List<Team> teams = new ArrayList<>();
        Team team1 = new Team();
        Team team2 = new Team();
        teams.add(team1);
        teams.add(team2);

        when(teamRepository.findAllTeamsInDescendingOrder()).thenReturn(teams);

        List<Team> result = teamService.findAllTeamsInDescendingOrder();
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
