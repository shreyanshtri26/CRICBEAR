package com.example.tournament.service;

import com.example.tournament.model.Innings;
import com.example.tournament.model.Match;
import com.example.tournament.model.PlayerRole;
import com.example.tournament.model.TossDecision;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.PlayerTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.example.tournament.service.BallByBallService.*;

@Service
public class InningsService {

    //static variables that can be used in the ballbyball service as well.
    protected static int tossWinTeam;
    public static int battingId;
    public static int bowlingId;
    public static TossDecision tossDecision;
    public static List<String> playing11Team1 = new ArrayList<>();
    public static List<String> bowlersTeam1;
    public static List<String> playing11Team2 = new ArrayList<>();
    public static List<String> bowlersTeam2;
    public static List<String> batsmenTeam1;
    public static List<String> batsmenTeam2;
    protected static int matchId;

    private static final Random RANDOM = new Random();


    @Autowired
    private InningsRepository inningsRepository;

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private PlayerTeamRepository playerTeamRepository;
    @Autowired
    private BallByBallService ballByBallService;

    //initializes all static lists to new.
    public static void setToNew() {
        playing11Team1 = new ArrayList<>();
        bowlersTeam1 = new ArrayList<>();
        playing11Team2 = new ArrayList<>();
        bowlersTeam2 = new ArrayList<>();
        batsmenTeam1 = new ArrayList<>();
        batsmenTeam2 = new ArrayList<>();
    }


    //Create a new innings.
    public Innings createInnings(Innings innings) {
        return inningsRepository.save(innings);
    }

    //Get all innings from the table.
    public List<Innings> getAll() {
        return inningsRepository.findAll();
    }

    //Get innings by match id. Each match has atmost 2 innings.
    public List<Innings> getById(Integer mid) {
        return inningsRepository.findByMid(mid);
    }


    //Get a random value from an enum.
    public static <T extends Enum<T>> T getRandomEnum(Class<T> clazz) {
        List<T> enumValues = Arrays.asList(clazz.getEnumConstants());
        int randomIndex = RANDOM.nextInt(enumValues.size());
        return enumValues.get(randomIndex);
    }

    //Set the batting team id, bowling team id and toss decision.
    public void tossDecision(Integer battingTeamId, Integer bowlingTeamId,TossDecision tossDecisionByTeam) {
        battingId = battingTeamId;
        bowlingId = bowlingTeamId;
        tossDecision = tossDecisionByTeam;
    }

    //To create the playing 11 of the first team.
    public void creationOfPlaying11Team1(){
        int limit =4;
        batsmenTeam1 = playerTeamRepository.findByRoleAndTeam(battingId, String.valueOf(PlayerRole.BATSMAN),limit);
        limit =  4 - batsmenTeam1.size();
        if(limit <=0)
            limit=0;
        List<String> allRounderTeam1 = playerTeamRepository.findByRoleAndTeam(battingId, String.valueOf(PlayerRole.ALLROUNDER),limit + 1);
        limit = limit - allRounderTeam1.size();
        if(limit<=0)
            limit = 0;
        List<String> wicketKeeperTeam1 = playerTeamRepository.findByRoleAndTeam(battingId, String.valueOf(PlayerRole.WICKETKEEPER),limit+1);
        limit = limit - wicketKeeperTeam1.size();
        if (limit <=0)
            limit = 0;
        bowlersTeam1 = playerTeamRepository.findByRoleAndTeam(battingId, String.valueOf(PlayerRole.BOWLER), 5);

        playing11Team1.addAll(batsmenTeam1);
        playing11Team1.addAll(allRounderTeam1);
        playing11Team1.addAll(wicketKeeperTeam1);
        playing11Team1.addAll(bowlersTeam1);
        bowlersTeam1.addAll(allRounderTeam1);
    }


    //To create the playing 11 of the second team.
    public void creationOfPlaying11Team2(){
        int limit =4;
        batsmenTeam2 = playerTeamRepository.findByRoleAndTeam(bowlingId, String.valueOf(PlayerRole.BATSMAN),limit);
        limit = limit - batsmenTeam2.size();
        if(limit<=0)
            limit=0;
        List<String> allrounderTeam2 = playerTeamRepository.findByRoleAndTeam(bowlingId, String.valueOf(PlayerRole.ALLROUNDER),limit+1);
        limit = limit - allrounderTeam2.size();
        if(limit<=0)
            limit = 0;
        List<String> wicketkeeperTeam2 = playerTeamRepository.findByRoleAndTeam(bowlingId, String.valueOf(PlayerRole.WICKETKEEPER),limit+1);
        limit = limit - wicketkeeperTeam2.size();
        if (limit <=0)
            limit = 0;
        bowlersTeam2 = playerTeamRepository.findByRoleAndTeam(bowlingId, String.valueOf(PlayerRole.BOWLER),limit+5);


        playing11Team2.addAll(batsmenTeam2);
        playing11Team2.addAll(allrounderTeam2);
        playing11Team2.addAll(wicketkeeperTeam2);
        playing11Team2.addAll(bowlersTeam2);
        bowlersTeam2.addAll(allrounderTeam2);
    }

    //Initializes the state for ballByBall service class
    public void initializeBallByBallState(int mid){
        BallByBallService.playing11= playing11Team1;
        Innings inn = inningsRepository.getByMid(mid);
        BallByBallService.iid= inn.getIid();
        BallByBallService.bowler= bowlersTeam2;
        BallByBallService.runs=0;
        BallByBallService.overs = 0;
        BallByBallService.wickets = 0;
        BallByBallService.ballNumber = 0;
        BallByBallService.isNoBall = false;
        BallByBallService.target=Integer.MAX_VALUE;
        BallByBallService.batterScore = 0;
        BallByBallService.batsman1Name = playing11.get(batterScore);
        BallByBallService.batterScore++;
        BallByBallService.batsman2Name = playing11.get(batterScore);
        BallByBallService.batterScore++;
        BallByBallService.bowlerName = bowler.get(RANDOM.nextInt(bowler.size()));
        BallByBallService.bowler.remove(bowlerName);
        BallByBallService.enabled=true;
        ballByBallService.startTask1();
    }

/*
 * Starts a cricket match by performing toss, setting up team configurations, and initializing values.
 *
 * This method performs the following actions:
 * 1. Retrieves the match details using the provided match ID.
 * 2. Randomly determines the toss decision and updates the toss-winning team accordingly.
 * 3. Saves the innings information to the repository.
 * 4. Sets up the playing 11 for both teams.
 * 5. Initializes the ball-by-ball class values and starts the innings.
 */
 public void startMatchWithTossAndSetup(Integer mid) {
        matchId = mid;
        Match match = matchRepository.getByMid(mid);
        TossDecision decision = getRandomEnum(TossDecision.class);
        if (decision == TossDecision.BAT) {
            tossWinTeam = match.getTeamId1();
            tossDecision(match.getTeamId1(),match.getTeamId2(),decision);
        }
        else {
            tossWinTeam = match.getTeamId2();
            tossDecision(match.getTeamId1(),match.getTeamId2(),decision);
        }
        Innings innings = new Innings(mid, battingId, bowlingId);
        inningsRepository.save(innings);

        //Create the playing 11 of both teams.
        creationOfPlaying11Team1();
        creationOfPlaying11Team2();

        //Setup the values and start the innings.
        initializeBallByBallState(mid);

    }

    //update the innings with runs, wickets and overs
    public Innings update(int iid, int runs, int wickets, double overToSave) {
        Innings innings = inningsRepository.getByIid(iid);
        innings.setRuns(runs);
        innings.setWickets(wickets);
        innings.setOvers(overToSave);
        return inningsRepository.save(innings);
    }
}