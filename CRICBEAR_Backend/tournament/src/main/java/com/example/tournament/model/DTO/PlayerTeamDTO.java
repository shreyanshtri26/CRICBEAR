package com.example.tournament.model.DTO;

import com.example.tournament.model.PlayerRole;
import com.example.tournament.model.PlayerTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTeamDTO {
    private int teamId;
    private int pid;
    private String teamName;
    private String name;
    private boolean overseas;
    private PlayerRole playerRole;
    private int runsScored;
    private int balls;
    private int wickets;
    private double overs;
    private int runsGiven;
}