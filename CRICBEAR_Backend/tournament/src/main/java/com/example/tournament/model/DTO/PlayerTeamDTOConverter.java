package com.example.tournament.model.DTO;

import com.example.tournament.model.PlayerRole;

public class PlayerTeamDTOConverter {
    public static PlayerTeamDTO convert(Object[] row) {
        if (row == null || row.length < 6) {
            throw new IllegalArgumentException("Invalid data provided to convert.");
        }


        int teamId = (Integer) row[0];
        int pid = (Integer) row[1];
        String teamName = (String) row[2];
        String name = (String) row[3];
        boolean overseas = (Boolean) row[4];
        PlayerRole playerRole = (PlayerRole) row[5];
        int runsScored = (Integer) row[6];
        int balls = (Integer) row[7];
        int wickets = (Integer) row[8];
        double overs = (Double) row[9];
        int runsGiven = (Integer) row[10];

        return new PlayerTeamDTO(teamId, pid, teamName, name, overseas, playerRole,runsScored,balls,wickets,overs,runsGiven);
    }
}

