package com.example.tournament.service;

import com.example.tournament.model.RegTeam;
import com.example.tournament.repository.RegTeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegTeamServiceTest {

    @Mock
    private RegTeamRepository regTeamRepository;

    @InjectMocks
    private RegTeamService regTeamService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRegTeam_Success() {
        RegTeam regTeam = new RegTeam();
        regTeam.setTid(1);
        regTeam.setTeamid(101);

        when(regTeamRepository.findByTidAndTeamId(1, 101)).thenReturn(null);
        when(regTeamRepository.countByTid(1)).thenReturn(2);
        when(regTeamRepository.save(any(RegTeam.class))).thenReturn(regTeam);

        RegTeam result = regTeamService.addTeamToTournament(regTeam);

        assertNotNull(result);
        assertEquals(1, result.getGroupNumber());
    }

    @Test
    public void testAddRegTeam_TeamAlreadyRegistered() {
        RegTeam regTeam = new RegTeam();
        regTeam.setTid(1);
        regTeam.setTeamid(101);

        when(regTeamRepository.findByTidAndTeamId(1, 101)).thenReturn(new RegTeam());

        RegTeam result = regTeamService.addTeamToTournament(regTeam);

        assertNull(result);
    }

    @Test
    public void testAddRegTeam_TournamentFull() {
        RegTeam regTeam = new RegTeam();
        regTeam.setTid(1);
        regTeam.setTeamid(101);

        when(regTeamRepository.findByTidAndTeamId(1, 101)).thenReturn(null);
        when(regTeamRepository.countByTid(1)).thenReturn(7);

        RegTeam result = regTeamService.addTeamToTournament(regTeam);

        assertNull(result);
    }

    @Test
    public void testGetAllRegTeams() {
        List<RegTeam> regTeams = new ArrayList<>();
        regTeams.add(new RegTeam());
        regTeams.add(new RegTeam());

        when(regTeamRepository.findAll()).thenReturn(regTeams);

        List<RegTeam> result = regTeamService.getAllRegTeams();

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByGroupNumber() {
        ArrayList<RegTeam> regTeams = new ArrayList<>();
        regTeams.add(new RegTeam());
        regTeams.add(new RegTeam());

        when(regTeamRepository.findByGroupNumber(1, 1)).thenReturn(regTeams);

        ArrayList<RegTeam> result = regTeamService.findByGroupNumber(1, 1);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindById_Found() {
        RegTeam regTeam = new RegTeam();
        regTeam.setTid(1);

        when(regTeamRepository.findById(1)).thenReturn(Optional.of(regTeam));

        RegTeam result = regTeamService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getTid());
    }

    @Test
    public void testFindById_NotFound() {
        when(regTeamRepository.findById(1)).thenReturn(Optional.empty());

        RegTeam result = regTeamService.findById(1);

        assertNull(result);
    }

    @Test
    public void testUpdateRegTeam() {
        RegTeam regTeam = new RegTeam();
        regTeam.setTeamid(1);

        when(regTeamRepository.save(any(RegTeam.class))).thenReturn(regTeam);

        RegTeam result = regTeamService.updateRegTeam(regTeam);

        assertNotNull(result);
        assertEquals(1, result.getTeamid());
    }

    @Test
    public void testDeleteRegTeam() {
        doNothing().when(regTeamRepository).deleteById(1);

        regTeamService.deleteRegTeam(1);

        verify(regTeamRepository, times(1)).deleteById(1);
    }
}
