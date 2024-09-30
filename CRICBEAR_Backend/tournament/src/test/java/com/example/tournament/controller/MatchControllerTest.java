package com.example.tournament.controller;

import com.example.tournament.model.DTO.MatchDTO;
import com.example.tournament.model.Match;
import com.example.tournament.model.MatchStatus;
import com.example.tournament.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class MatchControllerTest {

    @InjectMocks
    private MatchController matchController;

    @Mock
    private MatchService matchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSchedule() {
        int tid = 1;
        int uid = 1;
        ArrayList<Match> matches = new ArrayList<>();
        when(matchService.scheduleMatches(tid, uid)).thenReturn(matches);

        ResponseEntity<ArrayList<Match>> response = matchController.schedule(tid, uid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(matches, response.getBody());
    }

    @Test
    public void testSemiFinal() {
        int tid = 1;
        int uid = 1;
        ArrayList<Match> semiFinals = new ArrayList<>();
        when(matchService.scheduleSemiFinal(tid, uid)).thenReturn(semiFinals);

        ResponseEntity<ArrayList<Match>> response = matchController.semiFinal(tid, uid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(semiFinals, response.getBody());
    }

    @Test
    public void testStart() {
        int mid = 1;
        String result = "Match started";
        try {
            when(matchService.startMatch(mid)).thenReturn(result);

            ResponseEntity<String> response = matchController.start(mid);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(result, response.getBody());
        } catch (InterruptedException e) {
            fail("InterruptedException should not occur in test");
        }
    }


    @Test
    public void testFinalMatch() {
        int tid = 1;
        int uid = 1;
        Match finalMatch = new Match();
        when(matchService.finalSchedule(tid, uid)).thenReturn(finalMatch);

        ResponseEntity<Match> response = matchController.finalMatch(tid, uid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(finalMatch, response.getBody());
    }

    @Test
    public void testUpdate() {
        int mid = 1;
        Match match = new Match();
        Match updatedMatch = new Match();
        when(matchService.updateMatchIfEligible(mid, match)).thenReturn(updatedMatch);

        ResponseEntity<Match> response = matchController.update(mid, match);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMatch, response.getBody());
    }

    @Test
    public void testStatus() {
        MatchStatus status = MatchStatus.UPCOMING;
        List<MatchDTO> matchDTOs = new ArrayList<>();
        when(matchService.getMatchesByStatus(status)).thenReturn(matchDTOs);

        ResponseEntity<List<MatchDTO>> response = matchController.status(status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(matchDTOs, response.getBody());
    }

    @Test
    public void testGetByTid() {
        int tid = 1;
        List<MatchDTO> matchDTOs = new ArrayList<>();
        when(matchService.findAllByTid(tid)).thenReturn(matchDTOs);

        ResponseEntity<List<MatchDTO>> response = matchController.getByTid(tid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(matchDTOs, response.getBody());
    }
}
