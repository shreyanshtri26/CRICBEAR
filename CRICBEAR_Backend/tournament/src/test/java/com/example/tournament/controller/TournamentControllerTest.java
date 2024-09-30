package com.example.tournament.controller;

import com.example.tournament.model.Tournament;
import com.example.tournament.service.MatchService;
import com.example.tournament.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TournamentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TournamentService tournamentService;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private TournamentController tournamentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tournamentController).build();
    }

    @Test
    public void testCreate() throws Exception {
        Tournament tournament = new Tournament();
        tournament.setTournamentName("Test Tournament");

        when(tournamentService.createTournament(any(Tournament.class))).thenReturn(tournament);

        mockMvc.perform(MockMvcRequestBuilders.post("/tournament/create")
                        .contentType("application/json")
                        .content("{\"tournamentName\":\"Test Tournament\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tournamentName").value("Test Tournament"))
                .andDo(print());
    }

    @Test
    public void testUpdate_Success() throws Exception {
        Tournament tournament = new Tournament();
        tournament.setTid(1);
        tournament.setTournamentName("Updated Tournament");

        when(tournamentService.update(any(Tournament.class), anyInt())).thenReturn(tournament);

        mockMvc.perform(MockMvcRequestBuilders.put("/tournament/update/1")
                        .contentType("application/json")
                        .content("{\"tid\":1,\"tournamentName\":\"Updated Tournament\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tournamentName").value("Updated Tournament"))
                .andDo(print());
    }

    @Test
    public void testUpdate_Failure() throws Exception {
        when(tournamentService.update(any(Tournament.class), anyInt())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/tournament/update/1")
                        .contentType("application/json")
                        .content("{\"tid\":1,\"tournamentName\":\"Non-existent Tournament\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetById() throws Exception {
        Tournament tournament = new Tournament();
        tournament.setTid(1);

        when(tournamentService.getById(1)).thenReturn(tournament);

        mockMvc.perform(MockMvcRequestBuilders.get("/tournament/getById/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tid").value(1))
                .andDo(print());
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        when(tournamentService.getById(1)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/tournament/getById/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetAll() throws Exception {
        List<Tournament> tournaments = new ArrayList<>();
        Tournament tournament1 = new Tournament();
        Tournament tournament2 = new Tournament();
        tournaments.add(tournament1);
        tournaments.add(tournament2);

        when(tournamentService.getAll()).thenReturn(tournaments);

        mockMvc.perform(MockMvcRequestBuilders.get("/tournament/getAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").isNotEmpty())
                .andDo(print());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tournament/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Deleted Successfully"))
                .andDo(print());

        verify(tournamentService, times(1)).delete(1);
    }

    @Test
    public void testGetByStatus() throws Exception {
        List<Tournament> tournaments = new ArrayList<>();
        Tournament tournament1 = new Tournament();
        Tournament tournament2 = new Tournament();
        tournaments.add(tournament1);
        tournaments.add(tournament2);

        when(tournamentService.getByStatus("LIVE")).thenReturn(tournaments);

        Map<String, String> status = new HashMap<>();
        status.put("status", "LIVE");

        mockMvc.perform(MockMvcRequestBuilders.post("/tournament/getByStatus")
                        .contentType("application/json")
                        .content("{\"status\":\"LIVE\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").isNotEmpty())
                .andDo(print());
    }


    @Test
    public void testStart_Failure() throws Exception {
        when(tournamentService.start(1, 1)).thenReturn(false);

        Map<String, Integer> request = new HashMap<>();
        request.put("tid", 1);
        request.put("uid", 1);

        mockMvc.perform(MockMvcRequestBuilders.post("/tournament/start")
                        .contentType("application/json")
                        .content("{\"tid\":1,\"uid\":1}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetByTid() throws Exception {
        Tournament tournament = new Tournament();
        tournament.setTid(10);

        when(tournamentService.getByTid(10)).thenReturn(tournament);

        mockMvc.perform(MockMvcRequestBuilders.get("/tournament/getByTid/10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tid").value(10))
                .andDo(print());
    }

    @Test
    public void testGetByTid_NotFound() throws Exception {
        when(tournamentService.getByTid(10)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/tournament/getByTid/10"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }
}
