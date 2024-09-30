package com.example.tournament.service;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import com.example.tournament.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentService tournamentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate_Tournament_Success() {
        Tournament tournament = new Tournament();
        tournament.setTournamentName("Test Tournament");
        tournament.setStartDate(LocalDateTime.now().minusDays(15));
        tournament.setEndDate(LocalDateTime.now().plusDays(15));

        when(tournamentRepository.findByTournamentName("Test Tournament")).thenReturn(null);
        when(tournamentRepository.save(tournament)).thenReturn(tournament);

        Tournament createdTournament = tournamentService.createTournament(tournament);

        assertNotNull(createdTournament);
        assertEquals("Test Tournament", createdTournament.getTournamentName());
    }

    @Test
    public void testCreate_Tournament_Failure_DurationTooShort() {
        Tournament tournament = new Tournament();
        tournament.setTournamentName("Short Tournament");
        tournament.setStartDate(LocalDateTime.now().minusDays(5));
        tournament.setEndDate(LocalDateTime.now().plusDays(10));

        when(tournamentRepository.findByTournamentName("Short Tournament")).thenReturn(null);

        Tournament createdTournament = tournamentService.createTournament(tournament);

        assertNull(createdTournament);
    }

    @Test
    public void testCreate_Tournament_Failure_NameExists() {
        Tournament tournament = new Tournament();
        tournament.setTournamentName("Existing Tournament");

        when(tournamentRepository.findByTournamentName("Existing Tournament")).thenReturn(new Tournament());

        Tournament createdTournament = tournamentService.createTournament(tournament);

        assertNull(createdTournament);
    }

    @Test
    public void testGetAll() {
        Tournament tournament1 = new Tournament();
        Tournament tournament2 = new Tournament();
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);

        when(tournamentRepository.findAll()).thenReturn(tournaments);

        List<Tournament> result = tournamentService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetById() {
        Tournament tournament = new Tournament();
        tournament.setUid(1);

        when(tournamentRepository.findByUid(1)).thenReturn(tournament);

        Tournament result = tournamentService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getUid());
    }

    @Test
    public void testGetByTid() {
        Tournament tournament = new Tournament();
        tournament.setTid(10);

        when(tournamentRepository.findByTid(10)).thenReturn(tournament);

        Tournament result = tournamentService.getByTid(10);

        assertNotNull(result);
        assertEquals(10, result.getTid());
    }

    @Test
    public void testUpdate_Success() {
        Tournament tournamentDetails = new Tournament();
        tournamentDetails.setTid(10);
        tournamentDetails.setUid(1);
        tournamentDetails.setTournamentName("Updated Tournament");
        tournamentDetails.setStartDate(LocalDateTime.now().minusDays(10));
        tournamentDetails.setEndDate(LocalDateTime.now().plusDays(10));
        tournamentDetails.setStatus(Status.UPCOMING);

        Tournament existingTournament = new Tournament();
        existingTournament.setTid(10);
        existingTournament.setUid(1);

        when(tournamentRepository.findByTid(10)).thenReturn(existingTournament);
        when(tournamentRepository.save(existingTournament)).thenReturn(tournamentDetails);

        Tournament updatedTournament = tournamentService.update(tournamentDetails, 1);

        assertNotNull(updatedTournament);
        assertEquals("Updated Tournament", updatedTournament.getTournamentName());
    }

    @Test
    public void testUpdate_Failure_IdMismatch() {
        Tournament tournamentDetails = new Tournament();
        tournamentDetails.setTid(10);
        tournamentDetails.setUid(2);

        Tournament existingTournament = new Tournament();
        existingTournament.setTid(10);
        existingTournament.setUid(1);

        when(tournamentRepository.findByTid(10)).thenReturn(existingTournament);

        Tournament updatedTournament = tournamentService.update(tournamentDetails, 1);

        assertNull(updatedTournament);
    }

    @Test
    public void testDelete() {
        Integer id = 1;

        doNothing().when(tournamentRepository).deleteById(id);

        tournamentService.delete(id);

        verify(tournamentRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetByStatus() {
        Status status = Status.LIVE;
        Tournament tournament1 = new Tournament();
        Tournament tournament2 = new Tournament();
        List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);

        when(tournamentRepository.findAllByStatus(status)).thenReturn(tournaments);

        List<Tournament> result = tournamentService.getByStatus("LIVE");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testStart_Success() {
        Tournament tournament = new Tournament();
        tournament.setTid(10);
        tournament.setUid(1);
        tournament.setStartDate(LocalDateTime.now().toLocalDate().atStartOfDay());
        tournament.setStatus(Status.UPCOMING);

        when(tournamentRepository.findByTid(10)).thenReturn(tournament);

        boolean started = tournamentService.start(10, 1);

        assertTrue(started);
        assertEquals(Status.LIVE, tournament.getStatus());
        verify(tournamentRepository).save(tournament);
    }

    @Test
    public void testStart_Failure() {
        Tournament tournament = new Tournament();
        tournament.setTid(10);
        tournament.setUid(2);
        tournament.setStartDate(LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(1));
        tournament.setStatus(Status.UPCOMING);

        when(tournamentRepository.findByTid(10)).thenReturn(tournament);

        boolean started = tournamentService.start(10, 1);

        assertFalse(started);
        assertEquals(Status.UPCOMING, tournament.getStatus());
        verify(tournamentRepository, never()).save(tournament);
    }
}
