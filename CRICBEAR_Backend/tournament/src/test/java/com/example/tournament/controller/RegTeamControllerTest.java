package com.example.tournament.controller;

import com.example.tournament.model.RegTeam;
import com.example.tournament.service.RegTeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class RegTeamControllerTest {

    @Mock
    private RegTeamService regTeamService;

    @InjectMocks
    private RegTeamController regTeamController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRegTeam_Success() {
        RegTeam regTeam = new RegTeam();
        regTeam.setTid(1);
        regTeam.setTeamid(101);

        when(regTeamService.addTeamToTournament(any(RegTeam.class))).thenReturn(regTeam);

        ResponseEntity<RegTeam> response = regTeamController.addRegTeam(regTeam);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testAddRegTeam_Failure() {
        RegTeam regTeam = new RegTeam();
        regTeam.setTid(1);
        regTeam.setTeamid(101);

        when(regTeamService.addTeamToTournament(any(RegTeam.class))).thenReturn(null);

        ResponseEntity<RegTeam> response = regTeamController.addRegTeam(regTeam);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetAll_Success() {
        List<RegTeam> regTeams = new ArrayList<>();
        regTeams.add(new RegTeam());
        regTeams.add(new RegTeam());

        when(regTeamService.getAllRegTeams()).thenReturn(regTeams);

        ResponseEntity<List<RegTeam>> response = regTeamController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetAll_NoContent() {
        // Arrange: Mock the service to return an empty list
        when(regTeamService.getAllRegTeams()).thenReturn(new ArrayList<>());

        // Act: Call the controller method
        ResponseEntity<List<RegTeam>> response = regTeamController.getAll();

        // Assert: Check that the response status is NO_CONTENT and body is null
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteRegTeam_Success() {
        RegTeam regTeam = new RegTeam();
        regTeam.setTid(1);
        regTeam.setTeamid(101);

        doNothing().when(regTeamService).deleteRegTeam(anyInt());

        ResponseEntity<String> response = regTeamController.deleteRegTeam(regTeam);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted", response.getBody());
    }

    @Test
    public void testDeleteRegTeam_BadRequest() {
        ResponseEntity<String> response = regTeamController.deleteRegTeam(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
