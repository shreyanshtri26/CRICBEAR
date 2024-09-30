package com.example.tournament.service;

import com.example.tournament.model.DTO.MatchResultDTO;
import com.example.tournament.model.Innings;
import com.example.tournament.model.MatchResult;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.MatchResultRepository;
import com.example.tournament.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchResultService {
    @Autowired
    private MatchResultRepository matchResultRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private InningsRepository inningsRepository;

    //Create a new entry.
    public MatchResult createMatchResult(MatchResult matchResult) {
        return matchResultRepository.save(matchResult);
    }

    //Get all rows in the table.
    public List<MatchResult> getAllMatchResults() {
        return matchResultRepository.findAll();
    }

    //Get a match result by id.
    public MatchResult getMatchResultById(Integer id) {
        return matchResultRepository.findById(id).orElse(null);
    }

    //Update an existing match result.
    public MatchResult updateMatchResult(MatchResult updateMatchResult) {
        return matchResultRepository.save(updateMatchResult);
    }

    //Delete.
    public void deleteMatchResult(MatchResult matchResult) {
        matchResultRepository.delete(matchResult);
    }

    //Get by a particular match.
    public MatchResult getMatchResultByMatchId(Integer matchId) {

        return matchResultRepository.findByMid(matchId);
    }

    //Get the results by match id.
    public MatchResultDTO getResultByMatchId(Integer matchId) {
        MatchResult matchResult = matchResultRepository.findByMid(matchId);
        List<Innings> innings = inningsRepository.findByMid(matchId);
        Innings firstInning;
        Innings secondInning;
        if(innings.size()==2) {
            firstInning = innings.get(0);
            secondInning = innings.get(1);
        }
        else
            return null;
        return new MatchResultDTO(matchId,
                matchResult.getTossDesicion(),
                teamRepository.findByTeamId(matchResult.getTossWinTeam()).getTeamName(),
                teamRepository.findByTeamId(matchResult.getWinnerId()).getTeamName(),
                teamRepository.findByTeamId(matchRepository.getByMid(matchId).getTeamId1()).getTeamName(),
                teamRepository.findByTeamId(matchRepository.getByMid(matchId).getTeamId2()).getTeamName(),
                teamRepository.findByTeamId(firstInning.getBattingId()).getTeamName(),
                teamRepository.findByTeamId(secondInning.getBattingId()).getTeamName(),
                firstInning.getRuns(), firstInning.getWickets(), secondInning.getRuns(), secondInning.getWickets());
    }

}
