package com.example.tournament.controller;

import com.example.tournament.model.Team;
import com.example.tournament.service.TeamService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();
    }

    @Test
    public void testCreate() throws Exception {
        Team team = new Team();
        team.setTeamName("Team A");

        when(teamService.createTeam(any(Team.class))).thenReturn(team);

        mockMvc.perform(MockMvcRequestBuilders.post("/team/create")
                        .contentType("application/json")
                        .content("{\"teamName\":\"Team A\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamName").value("Team A"));
    }

    @Test
    public void testUpdate_Success() throws Exception {
        Team team = new Team();
        team.setTeamId(1);
        team.setTeamName("Updated Team");

        when(teamService.update(anyInt(), any(Team.class))).thenReturn(team);

        mockMvc.perform(MockMvcRequestBuilders.put("/team/update")
                        .contentType("application/json")
                        .content("{\"teamId\":1,\"teamName\":\"Updated Team\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamName").value("Updated Team"));
    }

    @Test
    public void testUpdate_Failure() throws Exception {
        when(teamService.update(anyInt(), any(Team.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/team/update")
                        .contentType("application/json")
                        .content("{\"teamId\":1,\"teamName\":\"Nonexistent Team\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetByTeamName_Found() throws Exception {
        Team team = new Team();
        team.setTeamName("Team B");

        when(teamService.getByTeamName("Team B")).thenReturn(team);

        mockMvc.perform(MockMvcRequestBuilders.get("/team/getByName/Team B"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamName").value("Team B"));
    }

    @Test
    public void testGetByTeamName_NotFound() throws Exception {
        when(teamService.getByTeamName("Team C")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/team/getByName/Team C"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAll() throws Exception {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team());
        teams.add(new Team());

        when(teamService.getAll()).thenReturn(teams);

        mockMvc.perform(MockMvcRequestBuilders.get("/team/getAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetByID_Found() throws Exception {
        Team team = new Team();
        team.setTeamId(1);

        when(teamService.getById(1)).thenReturn(team);

        mockMvc.perform(MockMvcRequestBuilders.get("/team/getByID/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamId").value(1));
    }

    @Test
    public void testGetByID_NotFound() throws Exception {
        when(teamService.getById(1)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/team/getByID/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(teamService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/team/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Deleted Successfully"));
    }

    @Test
    public void testFindInDesc() throws Exception {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team());
        teams.add(new Team());

        when(teamService.findAllTeamsInDescendingOrder()).thenReturn(teams);

        mockMvc.perform(MockMvcRequestBuilders.get("/team/findInDesc"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }
}
