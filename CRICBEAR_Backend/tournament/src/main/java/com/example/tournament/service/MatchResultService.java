package com.example.tournament.service;

import com.example.tournament.model.DTO.MatchResultDTO;
import com.example.tournament.model.Innings;
import com.example.tournament.model.Match;
import com.example.tournament.model.MatchResult;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.MatchResultRepository;
import com.example.tournament.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
                matchResult.getWinnerId() == null ? null : teamRepository.findByTeamId(matchResult.getWinnerId()).getTeamName(),
                teamRepository.findByTeamId(matchRepository.getByMid(matchId).getTeamId1()).getTeamName(),
                teamRepository.findByTeamId(matchRepository.getByMid(matchId).getTeamId2()).getTeamName(),
                teamRepository.findByTeamId(firstInning.getBattingId()).getTeamName(),
                teamRepository.findByTeamId(secondInning.getBattingId()).getTeamName(),
                firstInning.getRuns(), firstInning.getWickets(), secondInning.getRuns(), secondInning.getWickets());
    }


    //Results of every finished match in four queries (the per-match lookup above costs about twenty).
    public List<MatchResultDTO> getAllResults() {
        Map<Integer, String> names = new HashMap<>();
        teamRepository.findAll().forEach(t -> names.put(t.getTeamId(), t.getTeamName()));
        Map<Integer, Match> matches = new HashMap<>();
        matchRepository.findAll().forEach(m -> matches.put(m.getMid(), m));
        Map<Integer, List<Innings>> innings = new HashMap<>();
        inningsRepository.findAll().stream().sorted(Comparator.comparing(Innings::getIid))
                .forEach(i -> innings.computeIfAbsent(i.getMid(), k -> new ArrayList<>()).add(i));
        List<MatchResultDTO> out = new ArrayList<>();
        for (MatchResult r : matchResultRepository.findAll()) {
            List<Innings> two = innings.getOrDefault(r.getMid(), List.of());
            Match m = matches.get(r.getMid());
            if (two.size() != 2 || m == null) continue;
            out.add(new MatchResultDTO(r.getMid(), r.getTossDesicion(), names.get(r.getTossWinTeam()),
                    r.getWinnerId() == null ? null : names.get(r.getWinnerId()),
                    names.get(m.getTeamId1()), names.get(m.getTeamId2()),
                    names.get(two.get(0).getBattingId()), names.get(two.get(1).getBattingId()),
                    two.get(0).getRuns(), two.get(0).getWickets(), two.get(1).getRuns(), two.get(1).getWickets()));
        }
        return out;
    }
}
