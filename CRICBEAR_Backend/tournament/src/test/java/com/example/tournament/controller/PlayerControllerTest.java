package com.example.tournament.controller;

import com.example.tournament.model.Player;
import com.example.tournament.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.example.tournament.model.PlayerRole.BATSMAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    private Player player;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        player = new Player();
        player.setPid(1);
        player.setName("John Doe");
        player.setOverseas(false);
        player.setPlayerRole(BATSMAN);
    }

    @Test
    void testCreatePlayer_Success() {
        when(playerService.create(any(Player.class))).thenReturn(player);

        ResponseEntity<Player> response = playerController.create(player);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(player, response.getBody());
    }

    @Test
    void testCreatePlayer_BadRequest() {
        ResponseEntity<Player> response = playerController.create(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }




    @Test
    void testUpdatePlayer_NotFound() {
        when(playerService.update(Math.toIntExact(anyLong()), any(Player.class))).thenReturn(null);

        ResponseEntity<Player> response = playerController.update(player);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllPlayers_Success() {
        List<Player> players = Arrays.asList(player);
        when(playerService.getAll()).thenReturn(players);

        ResponseEntity<List<Player>> response = playerController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(players, response.getBody());
    }

    @Test
    void testGetAllPlayers_NoContent() {
        when(playerService.getAll()).thenReturn(null);

        ResponseEntity<List<Player>> response = playerController.getAll();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}