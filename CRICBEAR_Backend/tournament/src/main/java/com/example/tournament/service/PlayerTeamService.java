package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.PlayerTeamDTO;
import com.example.tournament.model.DTO.PlayerTeamDTOConverter;
import com.example.tournament.repository.PlayerRepository;
import com.example.tournament.repository.PlayerTeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerTeamService {

    @Autowired
    private PlayerTeamRepository playerTeamRepository;


    @Autowired
    private PlayerRepository playerRepository;

    // Retrieve all player teams
    public List<PlayerTeam> getAllPlayerTeams() {
        return playerTeamRepository.findAll();
    }

    // Get a specific player team by ID
    public Optional<PlayerTeam> getPlayerTeamById(int tpid) {
        return playerTeamRepository.findById(tpid);
    }

    // Get all player teams for a specific team ID
    public List<PlayerTeamDTO> getPlayerTeamsByTeamId(int teamId) {
        List<Object[]> results = playerTeamRepository.findPlayerTeamByTeamId(teamId);
        List<PlayerTeamDTO> playerTeamDTOs = new ArrayList<>();

        for (Object[] result : results) {
            PlayerTeamDTO playerTeamDTO = PlayerTeamDTOConverter.convert(result);
            playerTeamDTOs.add(playerTeamDTO);
        }

        return playerTeamDTOs;
    }


    //Save the list of players to a team if overseas < 5 and count < 15.
    public List<PlayerTeam> savePlayerstoTeam(List<PlayerTeam> playerTeam) {
        List<PlayerTeam> playerTeams = new ArrayList<>();
        for (PlayerTeam playerTeam1 : playerTeam) {
            int teamId = playerTeam1.getTeamId();
            Long count = playerTeamRepository.countByOverseas(teamId);
            if(count > 5)
                return new ArrayList<>();
            int count1 = playerTeamRepository.countByTeamId(teamId);
            if(count1 > 15)
                return new ArrayList<>();
            playerTeams.add(playerTeam1);
            Player player = playerRepository.findById(playerTeam1.getPid()).get();
            player.setFlag(true);
            playerTeam1.setFlag(true);
            playerRepository.save(player);
            playerTeamRepository.save(playerTeam1);
        }
        return playerTeams;
    }

    // Update an existing player by adding the runs scored /given by him.
    public PlayerTeam updatePlayerTeam(int runsScored,int ballsFaced,int wickets,int overs,int value,int pid) {
        PlayerTeam existingPlayerTeam = playerTeamRepository.findByPid(pid);
        Player existingPlayer = playerRepository.findByPid(pid);
        if (existingPlayer != null) {

            // Update player details
            existingPlayerTeam.setRunsScored(runsScored + existingPlayerTeam.getRunsScored());
            existingPlayerTeam.setBalls(ballsFaced + existingPlayerTeam.getBalls());
            existingPlayerTeam.setWickets(wickets + existingPlayerTeam.getWickets());
            existingPlayerTeam.setOvers(overs + existingPlayerTeam.getOvers());
            existingPlayerTeam.setRunsGiven(value + existingPlayerTeam.getRunsGiven());

            return playerTeamRepository.save(existingPlayerTeam);
        } else {
            return null; // Handle case where the player team is not found
        }
    }

    // Delete a player team by ID
    @Transactional
    public int deletePlayerTeam(int pid) {
        PlayerTeam player = playerTeamRepository.findById(pid).get();
        player.setFlag(false);
        return playerTeamRepository.deleteByPid(pid);
    }

    // Count players by team ID
    public int countByTeamId(int teamId) {
        return playerTeamRepository.countByTeamId(teamId);
    }
}
