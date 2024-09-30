package com.example.tournament.controller;

import com.example.tournament.controller.PlayerTeamController;
import com.example.tournament.model.PlayerTeam;
import com.example.tournament.model.DTO.PlayerTeamDTO;
import com.example.tournament.service.PlayerTeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PlayerTeamControllerTest {

    @Mock
    private PlayerTeamService playerTeamService;

    @InjectMocks
    private PlayerTeamController playerTeamController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(playerTeamController).build();
    }

    @Test
    public void testGetAllPlayerTeams() throws Exception {
        // Arrange
        PlayerTeam playerTeam = new PlayerTeam();
        List<PlayerTeam> playerTeams = new ArrayList<>();
        playerTeams.add(playerTeam);
        when(playerTeamService.getAllPlayerTeams()).thenReturn(playerTeams);

        // Act & Assert
        mockMvc.perform(get("/playerTeams/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pid").value(playerTeam.getPid()));
    }

    @Test
    public void testGetAllPlayerTeams_NoContent() throws Exception {
        // Arrange
        when(playerTeamService.getAllPlayerTeams()).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/playerTeams/all"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetPlayerTeamById() throws Exception {
        // Arrange
        int tpid = 1;
        PlayerTeam playerTeam = new PlayerTeam();
        when(playerTeamService.getPlayerTeamById(tpid)).thenReturn(Optional.of(playerTeam));

        // Act & Assert
        mockMvc.perform(get("/playerTeams/{tpid}", tpid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").value(playerTeam.getPid()));
    }

    @Test
    public void testGetPlayerTeamById_NotFound() throws Exception {
        // Arrange
        int tpid = 1;
        when(playerTeamService.getPlayerTeamById(tpid)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/playerTeams/{tpid}", tpid))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testAddPlayerTeam_InternalServerError() throws Exception {
        // Arrange
        List<PlayerTeam> playerTeams = new ArrayList<>();
        when(playerTeamService.savePlayerstoTeam(playerTeams)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/playerTeams/addPlayerTeam")
                        .contentType("application/json")
                        .content("[]"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void testDeletePlayerTeam() throws Exception {
        // Arrange
        int pid = 1;
        when(playerTeamService.deletePlayerTeam(pid)).thenReturn(1);

        // Act & Assert
        mockMvc.perform(delete("/playerTeams/delete/{pid}", pid))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePlayerTeam_NotFound() throws Exception {
        // Arrange
        int pid = 1;
        when(playerTeamService.deletePlayerTeam(pid)).thenReturn(0);

        // Act & Assert
        mockMvc.perform(delete("/playerTeams/delete/{pid}", pid))
                .andExpect(status().isNotFound());
    }



    @Test
    public void testGetPlayerTeamsByTeamId_NoContent() throws Exception {
        // Arrange
        int teamId = 1;
        when(playerTeamService.getPlayerTeamsByTeamId(teamId)).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/playerTeams/team/{teamId}", teamId))
                .andExpect(status().isNoContent());
    }
}
