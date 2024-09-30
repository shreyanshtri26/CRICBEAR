package com.example.tournament.service;

import com.example.tournament.model.Match;
import com.example.tournament.model.MatchStatus;
import com.example.tournament.model.Team;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    //Create a new team with a unique Team name.
    public Team createTeam(Team team) {
        if (teamRepository.findByTeamName(team.getTeamName()) == null)
            return teamRepository.save(team);
        return null;
    }

    //Return a team by id.
    public Team getById(int id) {
        return teamRepository.findById(id).orElse(null);
    }

    //Return all teams.
    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    //Return by team name.
    public Team getByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }

    //Update the team details and return.
    public Team update(int teamId, Team updatedTeam) {
        // Retrieve the existing team by ID
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        existingTeam.setTeamName(updatedTeam.getTeamName());
        existingTeam.setMatchesPlayed(updatedTeam.getMatchesPlayed());
        existingTeam.setMatchesWon(updatedTeam.getMatchesWon());
        existingTeam.setMatchesLost(updatedTeam.getMatchesLost());
        existingTeam.setMatchesDrawn(updatedTeam.getMatchesDrawn());
        existingTeam.setMatchesAbandoned(updatedTeam.getMatchesAbandoned());
        existingTeam.setPoints(updatedTeam.getPoints());
        existingTeam.setNrr(updatedTeam.getNrr());

        return teamRepository.save(existingTeam);
    }

    //Delete an existing team.
    public void delete(Integer id) {
        teamRepository.deleteById(id);
    }

    //Increase the points and other details after a match result for team stats.
    public Team updateAfterResults(Team teamBatting, int matchesPlayed, int matchesWon, int matchesLost, int matchesDrawn, int matchesAbandoned, int points, double nrr) {
        teamBatting.setMatchesPlayed(teamBatting.getMatchesPlayed() + matchesPlayed);
        teamBatting.setMatchesWon(teamBatting.getMatchesWon() + matchesWon);
        teamBatting.setMatchesLost(teamBatting.getMatchesLost() + matchesLost);
        teamBatting.setMatchesDrawn(teamBatting.getMatchesDrawn()+matchesDrawn);
        teamBatting.setMatchesAbandoned(teamBatting.getMatchesAbandoned()+matchesAbandoned);
        teamBatting.setPoints(teamBatting.getPoints()+points);
        teamBatting.setNrr(teamBatting.getNrr()+nrr);
        return teamRepository.save(teamBatting);
    }

    //update the status of a match.
    public void updateAfterResultsForMatch(int mid) {
        Match match = matchRepository.getByMid(mid);
        match.setStatus(MatchStatus.COMPLETED);
        matchRepository.save(match);
    }

    //Points table in descending order.
    public List<Team> findAllTeamsInDescendingOrder() {
        return teamRepository.findAllTeamsInDescendingOrder();
    }

    //Find a team by the coach id.
    public Team getByCoachId(int coachid) {
        return teamRepository.findByCoachId(coachid);
    }
}
