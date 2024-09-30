package com.example.tournament.service;

import com.example.tournament.model.DTO.PlayerTeamDTO;
import com.example.tournament.model.Player;
import com.example.tournament.model.PlayerTeam;
import com.example.tournament.repository.PlayerRepository;
import com.example.tournament.repository.PlayerTeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerTeamServiceTest {

    @Mock
    private PlayerTeamRepository playerTeamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerTeamService playerTeamService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPlayerTeams() {
        // Arrange
        PlayerTeam playerTeam = new PlayerTeam();
        List<PlayerTeam> playerTeams = new ArrayList<>();
        playerTeams.add(playerTeam);
        when(playerTeamRepository.findAll()).thenReturn(playerTeams);

        // Act
        List<PlayerTeam> result = playerTeamService.getAllPlayerTeams();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(playerTeam, result.get(0));
    }

    @Test
    public void testGetPlayerTeamById() {
        // Arrange
        int tpid = 1;
        PlayerTeam playerTeam = new PlayerTeam();
        when(playerTeamRepository.findById(tpid)).thenReturn(Optional.of(playerTeam));

        // Act
        Optional<PlayerTeam> result = playerTeamService.getPlayerTeamById(tpid);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(playerTeam, result.get());
    }

    @Test
    public void testGetPlayerTeamsByTeamId() {
        // Arrange
        int teamId = 1;
        List<Object[]> results = new ArrayList<>();
        // Mock result conversion
        when(playerTeamRepository.findPlayerTeamByTeamId(teamId)).thenReturn(results);

        // Act
        List<PlayerTeamDTO> result = playerTeamService.getPlayerTeamsByTeamId(teamId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void savePlayerstoTeam_ShouldReturnSavedPlayersIfCountValid() {
        PlayerTeam playerTeam1 = new PlayerTeam();
        playerTeam1.setTeamId(1);
        playerTeam1.setPid(1);
        when(playerTeamRepository.countByOverseas(1)).thenReturn(3L);
        when(playerTeamRepository.countByTeamId(1)).thenReturn(13);
        when(playerTeamRepository.save(any(PlayerTeam.class))).thenReturn(playerTeam1);
        when(playerRepository.findById(1)).thenReturn(Optional.of(new Player()));

        List<PlayerTeam> result = playerTeamService.savePlayerstoTeam(Arrays.asList(playerTeam1));

        assertEquals(1, result.size());
        verify(playerTeamRepository, times(1)).save(playerTeam1);
    }

    @Test
    void savePlayerstoTeam_ShouldReturnEmptyListIfOverseasLimitExceeded() {
        PlayerTeam playerTeam1 = new PlayerTeam();
        playerTeam1.setTeamId(1);
        playerTeam1.setPid(1);
        when(playerTeamRepository.countByOverseas(1)).thenReturn(6L);

        List<PlayerTeam> result = playerTeamService.savePlayerstoTeam(Arrays.asList(playerTeam1));

        assertTrue(result.isEmpty());
        verify(playerTeamRepository, never()).save(any());
    }



    @Test
    public void testDeletePlayerTeam() {
        // Given
        int playerId = 1;
        PlayerTeam player = new PlayerTeam();
        player.setPid(playerId);
        player.setFlag(true);

        when(playerTeamRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerTeamRepository.deleteByPid(playerId)).thenReturn(1);

        // When
        int result = playerTeamService.deletePlayerTeam(playerId);

        // Then
        verify(playerTeamRepository).findById(playerId);
        verify(playerTeamRepository).deleteByPid(playerId);

        assertFalse(player.isFlag(), "Player flag should be set to false");
        assertEquals(1, result, "The result should be 1");
    }

    @Test
    public void testCountByTeamId() {
        // Arrange
        int teamId = 1;
        int count = 5;
        when(playerTeamRepository.countByTeamId(teamId)).thenReturn(count);

        // Act
        int result = playerTeamService.countByTeamId(teamId);

        // Assert
        assertEquals(count, result);
    }
}
