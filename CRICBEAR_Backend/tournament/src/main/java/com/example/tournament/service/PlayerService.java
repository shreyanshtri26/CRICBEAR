package com.example.tournament.service;
import com.example.tournament.model.Player;
import com.example.tournament.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    //Create a new player.
    public Player create(Player player) {
        if (playerRepository.findByName(player.getName()) == null)
            return playerRepository.save(player);
        return null;
    }

    //List of all players in the database.
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    //Update the details of an existing player.
    public Player update(int playerId, Player updatedPlayer) {
        // Retrieve the existing player by ID
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        existingPlayer.setName(updatedPlayer.getName());
        existingPlayer.setOverseas(updatedPlayer.isOverseas());
        existingPlayer.setPlayerRole(updatedPlayer.getPlayerRole());


        return playerRepository.save(existingPlayer);
    }

    //Delete a player.
    public String delete(String playerName) {
        playerRepository.deleteByName(playerName);
        return "Player deleted";
    }
}