package com.example.tournament.controller;

//import com.example.tournament.model.Player;
import com.example.tournament.model.Player;
import com.example.tournament.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // Create a new player
    @PostMapping("/create")
    public ResponseEntity<Player> create(@RequestBody Player player) {
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player createdPlayer = playerService.create(player);
        if (createdPlayer != null) {
            return new ResponseEntity<>(createdPlayer, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing player
    @PutMapping("/update")
    public ResponseEntity<Player> update(@RequestBody Player player) {
        Player updatedPlayer = playerService.update(player.getPid(), player);
        if (updatedPlayer != null) {
            return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    // Get all players
    @GetMapping("/getAll")
    public ResponseEntity<List<Player>> getAll() {
        List<Player> players = playerService.getAll();
        if (players != null && !players.isEmpty()) {
            return new ResponseEntity<>(players, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        }
    }

    // Delete a player by name
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {
        playerService.delete(name);
        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }
}
