package com.example.tournament.service;

import com.example.tournament.model.RegTeam;
import com.example.tournament.repository.RegTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegTeamService {

    @Autowired
    private RegTeamRepository regTeamRepository;

    //Register a team to a tournament if limit is not reached and set the group number.
    public RegTeam addTeamToTournament(RegTeam regTeam) {
        int tid = regTeam.getTid();
        int teamId = regTeam.getTeamid();
        RegTeam regTeams = regTeamRepository.findByTidAndTeamId(tid,teamId);
        if(regTeams != null) {
            return null;
        }
        int count = regTeamRepository.countByTid(regTeam.getTid());

        if (count > 6) {
            return null;
        }
        if(count < 3) {
            regTeam.setGroupNumber(1);
        }
        else
            regTeam.setGroupNumber(2);
        return regTeamRepository.save(regTeam);
    }

    //Get all Registered teams.
    public List<RegTeam> getAllRegTeams() {
        return regTeamRepository.findAll();
    }

    //Return all teams of a particular group in a tournament.
    public ArrayList<RegTeam> findByGroupNumber(int groupNumber, int tid) {
        return regTeamRepository.findByGroupNumber(groupNumber,tid);
    }

    //
    public RegTeam findById(int id) {
        return regTeamRepository.findById(id).orElse(null);
    }

    public RegTeam updateRegTeam(RegTeam regTeam) {
        return regTeamRepository.save(regTeam);
    }
    public void deleteRegTeam(int id) {
        regTeamRepository.deleteById(id);
    }
}
