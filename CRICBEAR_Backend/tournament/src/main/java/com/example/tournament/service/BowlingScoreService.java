package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.repository.BowlingScoreRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BowlingScoreService {
    @Autowired
    private BowlingScoreRepository bowlingScoreRepository;

    @Autowired
    private PlayerTeamService playerTeamService;
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private InningsRepository inningsRepository;

    //Get all rows.
    public List<BowlingScore> getAll() {
        return bowlingScoreRepository.findAll();
    }

    //Create the rows for every bowler.
    public boolean create(List<String> bowlingScore, Integer iid) {
        for (String bs : bowlingScore) {
            bowlingScoreRepository.save(new BowlingScore(bs, 0L, 0,0, iid));
        }
        return true;
    }

    //Update the scorecard of the bowler.
    public void update(String bowlerId, Long value, int overs,int wickets,int iid) {
        BowlingScore existingBowlingScore = bowlingScoreRepository.findByNameAndIid(bowlerId,iid);
        if (existingBowlingScore !=null) {
            existingBowlingScore.setOvers(overs);
            existingBowlingScore.setRunsGiven(existingBowlingScore.getRunsGiven()+value);
            existingBowlingScore.setWickets(existingBowlingScore.getWickets()+wickets);
            bowlingScoreRepository.save(existingBowlingScore);
        }
    }

    //Update the player stats for that team.
    public void updatePlayerStatsForBowling(String bowlerid,int value,int overs,int wickets) {
        Player player = playerRepository.findByName(bowlerid);
        playerTeamService.updatePlayerTeam(0,1,wickets,overs,value,player.getPid());
    }

    //Get the data of both innings by match id.
    public Map<String, List<BowlingScore>> getAllDataByMid(int mid){
        List<Innings> innings = inningsRepository.findByMid(mid);
        Map<String, List<BowlingScore>> result = new HashMap<>();
        if(innings.isEmpty())
            return new HashMap<>();
        List<BowlingScore> bowlingScores = bowlingScoreRepository.findByIid(innings.get(0).getIid());
        result.put("Innings1", bowlingScores);
        if(innings.size()==1){
            return result;
        }
        List<BowlingScore> bowlingScores2 = bowlingScoreRepository.findByIid(innings.get(1).getIid());
        result.put("Innings2", bowlingScores2);
        return result;
    }
}