package com.example.tournament.service;

import com.example.tournament.model.BattingScore;
import com.example.tournament.model.Innings;
import com.example.tournament.model.Player;
import com.example.tournament.repository.BattingScoreRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BattingScoreService {
    @Autowired
    private BattingScoreRepository battingScoreRepository;
    @Autowired
    private PlayerTeamService playerTeamService;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private InningsRepository inningsRepository;

    //Return all rows.
    public List<BattingScore> getAll() {
        return battingScoreRepository.findAll();
    }

    //Create the rows for all players in a team.
    public boolean create(List<String> battingScore, Integer iid) {
        for (String bs : battingScore) {
            battingScoreRepository.save(new BattingScore(bs, 0L,0,0,0,false,iid));
        }
        return true;
    }

    //Update the scores for a batsman.
    public void update(String batsmanName, int runsScored, int ballsfaced, int fours, int six, boolean isOut, Integer iid) {
        BattingScore existingBattingScore = battingScoreRepository.findByNameAndIid(batsmanName,iid);
        if (existingBattingScore!=null) {
            existingBattingScore.setRunsScored(existingBattingScore.getRunsScored()+runsScored);
            existingBattingScore.setBallsFaced(existingBattingScore.getBallsFaced() + ballsfaced);
            existingBattingScore.setFours(existingBattingScore.getFours() + fours);
            existingBattingScore.setSix(existingBattingScore.getSix() + six);
            existingBattingScore.setIsOut(isOut);
            battingScoreRepository.save(existingBattingScore);
        }
    }


    //Update the scores of a batsman.
    public void updatePlayerStatsForBatting(String batsmanName, int runsScored, int ballsFaced, int wickets,int overs, int value) {
        Player player = playerRepository.findByName(batsmanName);
        playerTeamService.updatePlayerTeam(runsScored,ballsFaced,wickets,overs,value,player.getPid());
    }

    //get the results for both innings in a match.
    public Map<String, List<BattingScore>> getAllDataByMid(int mid){
        List<Innings> innings = inningsRepository.findByMid(mid);
        Map<String, List<BattingScore>> result = new HashMap<>();
        if(innings.isEmpty())
            return new HashMap<>();
        List<BattingScore> battingScores = battingScoreRepository.findByIid(innings.get(0).getIid());
        result.put("Innings1", battingScores);
        if(innings.size()==1){
            return result;
        }
        List<BattingScore> battingScores2 = battingScoreRepository.findByIid(innings.get(1).getIid());
        result.put("Innings2", battingScores2);
        return result;
    }
}
