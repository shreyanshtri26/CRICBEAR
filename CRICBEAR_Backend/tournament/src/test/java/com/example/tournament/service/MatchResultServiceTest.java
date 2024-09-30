package com.example.tournament.service;

import com.example.tournament.model.DTO.MatchResultDTO;
import com.example.tournament.model.Innings;
import com.example.tournament.model.Match;
import com.example.tournament.model.MatchResult;
import com.example.tournament.model.Team;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.MatchResultRepository;
import com.example.tournament.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchResultServiceTest {

    @Mock
    private MatchResultRepository matchResultRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private InningsRepository inningsRepository;

    @InjectMocks
    private MatchResultService matchResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMatchResult() {
        MatchResult matchResult = new MatchResult();
        when(matchResultRepository.save(any(MatchResult.class))).thenReturn(matchResult);

        MatchResult result = matchResultService.createMatchResult(matchResult);

        assertNotNull(result);
        verify(matchResultRepository).save(matchResult);
    }

    @Test
    void getAllMatchResults() {
        List<MatchResult> matchResults = Arrays.asList(new MatchResult(), new MatchResult());
        when(matchResultRepository.findAll()).thenReturn(matchResults);

        List<MatchResult> result = matchResultService.getAllMatchResults();

        assertEquals(2, result.size());
        verify(matchResultRepository).findAll();
    }

    @Test
    void getMatchResultById() {
        Integer id = 1;
        MatchResult matchResult = new MatchResult();
        when(matchResultRepository.findById(id)).thenReturn(Optional.of(matchResult));

        MatchResult result = matchResultService.getMatchResultById(id);

        assertNotNull(result);
        verify(matchResultRepository).findById(id);
    }

    @Test
    void updateMatchResult() {
        MatchResult matchResult = new MatchResult();
        when(matchResultRepository.save(any(MatchResult.class))).thenReturn(matchResult);

        MatchResult result = matchResultService.updateMatchResult(matchResult);

        assertNotNull(result);
        verify(matchResultRepository).save(matchResult);
    }

    @Test
    void deleteMatchResult() {
        MatchResult matchResult = new MatchResult();

        matchResultService.deleteMatchResult(matchResult);

        verify(matchResultRepository).delete(matchResult);
    }

    @Test
    void getMatchResultByMatchId() {
        Integer matchId = 1;
        MatchResult matchResult = new MatchResult();
        when(matchResultRepository.findByMid(matchId)).thenReturn(matchResult);

        MatchResult result = matchResultService.getMatchResultByMatchId(matchId);

        assertNotNull(result);
        verify(matchResultRepository).findByMid(matchId);
    }

}