package com.example.tournament.service;

import com.example.tournament.model.Player;
import com.example.tournament.model.PlayerRole;
import com.example.tournament.repository.PlayerRepository;
import com.example.tournament.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePlayer_Success() {
        // Arrange
        Player player = new Player();
        player.setName("John Doe");
        when(playerRepository.findByName(player.getName())).thenReturn(null);
        when(playerRepository.save(player)).thenReturn(player);

        // Act
        Player result = playerService.create(player);

        // Assert
        assertNotNull(result);
        assertEquals(player.getName(), result.getName());
        verify(playerRepository).save(player);
    }

    @Test
    public void testCreatePlayer_Failure() {
        // Arrange
        Player player = new Player();
        player.setName("John Doe");
        when(playerRepository.findByName(player.getName())).thenReturn(player);

        // Act
        Player result = playerService.create(player);

        // Assert
        assertNull(result);
        verify(playerRepository, never()).save(player);
    }

    @Test
    public void testGetAllPlayers() {
        // Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        List<Player> players = Arrays.asList(player1, player2);
        when(playerRepository.findAll()).thenReturn(players);

        // Act
        List<Player> result = playerService.getAll();

        // Assert
        assertEquals(2, result.size());
        verify(playerRepository).findAll();
    }

    @Test
    public void testUpdatePlayer_Success() {
        // Arrange
        int playerId = 1;
        Player existingPlayer = new Player();
        existingPlayer.setName("Old Name");
        Player updatedPlayer = new Player();
        updatedPlayer.setName("New Name");
        updatedPlayer.setPlayerRole(PlayerRole.BATSMAN);
        existingPlayer.setPlayerRole(PlayerRole.BOWLER);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.save(existingPlayer)).thenReturn(existingPlayer);

        // Act
        Player result = playerService.update(playerId, updatedPlayer);

        // Assert
        assertNotNull(result);
        assertEquals(updatedPlayer.getName(), result.getName());
        assertEquals(updatedPlayer.getPlayerRole(), result.getPlayerRole());
        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(existingPlayer);
    }

    @Test
    public void testUpdatePlayer_PlayerNotFound() {
        // Arrange
        int playerId = 1;
        Player updatedPlayer = new Player();
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            playerService.update(playerId, updatedPlayer);
        });
        assertEquals("Player not found", thrown.getMessage());
        verify(playerRepository).findById(playerId);
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testDeletePlayer_Success() {
        // Arrange
        String playerName = "John Doe";
        doNothing().when(playerRepository).deleteByName(playerName);

        // Act
        String result = playerService.delete(playerName);

        // Assert
        assertEquals("Player deleted", result);
        verify(playerRepository).deleteByName(playerName);
    }
}
