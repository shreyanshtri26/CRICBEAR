package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.InningsDTO;
import com.example.tournament.repository.BallByBallRepository;
import com.example.tournament.repository.InningsRepository;
import com.example.tournament.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.tournament.service.InningsService.*;


@Service
@Component
@EnableScheduling
@Data
@AllArgsConstructor
public class BallByBallService {

    @Autowired
    private BallByBallRepository ballByBallRepository;

    @Autowired
    private InningsRepository inningsRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchResultService matchResultService;

    @Autowired
    BattingScoreService battingScoreService;


    //Static variables
    public Thread taskThread;
    public static final ArrayList<Object> possibleOutcomes = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 6, "W", Extra.WIDE, Extra.NOBALL));
    private static final Random RANDOM = new Random();
    private static final long DELAY = 2000;
    public static boolean enabled = false;
    protected static boolean enabled2 = false;
    protected static int target = Integer.MAX_VALUE;
    protected static int runs = 0;
    protected static int wickets = 0;
    protected static int overs = 0;
    protected static int ballNumber = 0;
    protected static int batterScore = 0;
    public static List<String> playing11;
    protected static List<String> bowler;
    protected static int iid;
    public static String batsman1Name;
    public static String batsman2Name;
    public static String bowlerName;
    protected static Wicket wicketType;
    protected static boolean isNoBall;

    @Autowired
    private BowlingScoreService bowlingScoreService;


    //Create the batting scorecard for that innings.
    public void createBattingScorecard(List<String> playing11Team) {
        battingScoreService.create(playing11Team, iid);
    }

    //Create the bowling Scorecard.
    public void createBowlingScorecard(List<String> bowlerTeam) {
        bowlingScoreService.create(bowlerTeam, iid);
    }

    //Set the static variable values.
    public static void setStaticValues() {
        target = runs;
        runs = 0;
        BallByBallService.wickets = 0;
        BallByBallService.overs = 0;
        BallByBallService.ballNumber = 0;
        batterScore = 0;
        playing11 = playing11Team2;
        BallByBallService.batsman1Name = playing11.get(batterScore);
        batterScore++;
        BallByBallService.batsman2Name = playing11.get(batterScore);
        batterScore++;
        bowler = bowlersTeam1;
        BallByBallService.bowlerName = bowler.get(RANDOM.nextInt(bowler.size()));
        bowler.remove(bowlerName);
    }

    //Random value from te RANDOM list.
    public static <T extends Enum<T>> T getRandomEnum(Class<T> clazz) {
        List<T> enumValues = Arrays.asList(clazz.getEnumConstants());
        int randomIndex = RANDOM.nextInt(enumValues.size());
        return enumValues.get(randomIndex);
    }

    //Swap the batsmen.
    public static void swap() {
        String temp = batsman1Name;
        batsman1Name = batsman2Name;
        batsman2Name = temp;
    }


    //Update the scores of the players in a single transaction.
    //TO DO : Optimisation given by geetanshu is to save the details of 3 balls once in a single transaction to reduce DB interactions. Do only after the entire project is completed and have time.
    @Transactional
    public void updateDataAsASingleTransaction(Long value, int wickets, int runsScored, int ballsFaced, int four, int six, boolean isOut) {
        bowlingScoreService.update(bowlerName, value, ballByBallRepository.countByBowlerId(bowlerName, iid), wickets, iid);
        bowlingScoreService.updatePlayerStatsForBowling(bowlerName, Math.toIntExact(value), ballByBallRepository.countByBowlerId(bowlerName, iid), wickets);
        battingScoreService.updatePlayerStatsForBatting(batsman1Name, Math.toIntExact(value), ballsFaced, 0, 0, 0);
        battingScoreService.update(batsman1Name, Math.toIntExact(value), ballsFaced, four, six, isOut, iid);
    }


    //Extras handler.
    public void extraScoreHandler(Extra value) {
        if (Extra.valueOf(value.toString()).equals(Extra.NOBALL)) {
            isNoBall = true;
        }
        runs++;
        bowlingScoreService.update(bowlerName, Long.valueOf(1), ballByBallRepository.countByBowlerId(bowlerName, iid), 0, iid);
        ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Name, batsman2Name, bowlerName, null, Extra.valueOf(value.toString()), null, runs, wickets));
    }


    //Wicket Handler.
    public void wicketHandler() {
        if (!isNoBall) {
            wickets++;
            wicketType = getRandomEnum(Wicket.class);
            ballNumber++;
            if (wicketType.equals(Wicket.RUNOUT)) {
                updateDataAsASingleTransaction(0L, 0, 0, 1, 0, 0, true);
            } else {
                updateDataAsASingleTransaction(0L, 1, 0, 1, 0, 0, true);
            }
            ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Name, batsman2Name, bowlerName, null, null, wicketType, runs, wickets));
            if (wickets == 10) {
                return;
            } else {
                batsman1Name = playing11.get(batterScore);
                batterScore++;
            }
        } else {
            ballNumber++;
            ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Name, batsman2Name, bowlerName, null, null, wicketType, runs, wickets));
        }
        isNoBall = false;
    }

    //Runs scored handler.
    public void runsHandler(Integer value) {
        Integer four = 0;
        Integer six = 0;
        Integer integer = value;
        runs += value;
        if (value == 4)
            four = 1;
        if (value == 6)
            six = 1;
        isNoBall = false;
        ballNumber++;
        updateDataAsASingleTransaction(value.longValue(), 0, value, 1, four, six, false);
        ballByBallRepository.save(new BallByBall(iid, overs, ballNumber, batsman1Name, batsman2Name, bowlerName, integer, null, null, runs, wickets));
        if (integer % 2 != 0) {
            swap();
        }
    }

    //New Bowler selection after an over. No bowler can bowl two sonsecutive overs and more than 4 overs.
    public void overCompleteAndBowlerSelection() {
        if (ballByBallRepository.countByBowlerId(bowlerName, iid) >= 4) {
            bowlerName = null;
        }
        if (bowler.size() <= 2) {
            bowler.addAll(batsmenTeam2);
        }
        overs++;
        ballNumber = 0;
        String temp = bowler.get(RANDOM.nextInt(bowler.size()));
        if (bowlerName != null)
            bowler.add(bowlerName);
        bowlerName = temp;
        bowler.remove(bowlerName);
        swap();
    }

    //Updating and swaping the teams after the first innings.
    public void afterFirstInnings() {
        double overToSave = Double.parseDouble(overs + "." + ballNumber);
        Innings innings = new Innings(iid, matchId, battingId, bowlingId, runs, wickets, overToSave);
        inningsRepository.save(innings);
        Innings innings1 = new Innings(matchId, bowlingId, battingId);
        innings1 = inningsRepository.save(innings1);
        iid = innings1.getIid();
        setStaticValues();
        enabled = false;
        enabled2 = true;
        stopTask1();
        startTask2();
    }

    //Updating the match results and points after the match is over.
    //TO DO: Optimise by reducing the DB interactions.
    public void afterSecondInnings() {
        double overToSave = Double.parseDouble(overs + "." + ballNumber);
        Innings innings = new Innings(iid, matchId, bowlingId, battingId, runs, wickets, overToSave);
        inningsRepository.save(innings);
        setToNew();
        teamService.updateAfterResultsForMatch(matchId);
        Team teamBattingSecond = teamService.getById(bowlingId);
        Team teamBattingFirst = teamService.getById(battingId);
        if (target > 0) {
            double nrr = 0;
            if (target > 0 && target <= 25)
                nrr = 0.1;
            else if (target > 25 && target <= 50)
                nrr = 0.2;
            else
                nrr = 0.3;
            matchResultService.createMatchResult(new MatchResult(matchId, tossDecision, tossWinTeam, teamBattingFirst.getTeamId()));
            teamService.updateAfterResults(teamBattingFirst, 1, 1, 0, 0, 0, 2, nrr);
            teamService.updateAfterResults(teamBattingSecond, 1, 0, 1, 0, 0, 0, -nrr);
        } else if (target < 0) {
            double nrr = 0;
            int winByWickets = 10 - wickets;
            if (winByWickets > 0 && winByWickets <= 3)
                nrr = 0.1;
            else if (winByWickets > 3 && winByWickets <= 6)
                nrr = 0.2;
            else
                nrr = 0.3;
            matchResultService.createMatchResult(new MatchResult(matchId, tossDecision, tossWinTeam, teamBattingSecond.getTeamId()));
            teamService.updateAfterResults(teamBattingSecond, 1, 1, 0, 0, 0, 2, nrr);
            teamService.updateAfterResults(teamBattingFirst, 1, 0, 1, 0, 0, 0, -nrr);

        } else {
            teamService.updateAfterResults(teamBattingSecond, 1, 0, 0, 1, 0, 1, 0);
            teamService.updateAfterResults(teamBattingFirst, 1, 0, 0, 1, 0, 1, 0);
        }
        enabled2 = false;
        stopTask2();
    }

    //Score calculation of the first innings.
    //TO DO: Optimisation - Merge the two innings in one method with the help of target == Integer.MAX_VALUE. Do only after the entire project is completed and have time.
    public void calculateScoreForFirstInnings() throws InterruptedException {
        if (!enabled) {
        } else {

            if (overs < 20 && wickets < 10) {

                Object value = possibleOutcomes.get(RANDOM.nextInt(possibleOutcomes.size()));
                if (value instanceof Extra) {
                    extraScoreHandler((Extra) value);
                } else if (value instanceof String) {
                    if ("W".equals(value)) {
                        wicketHandler();
                    }
                } else if (value instanceof Integer) {
                    runsHandler((Integer) value);
                }
                if (ballNumber % 6 == 0 && ballNumber != 0) {
                    overCompleteAndBowlerSelection();
                }
            } else {
                afterFirstInnings();
            }
        }
    }

    //Thread to start innings one.
    public void startTask1() {
        if (taskThread == null || !taskThread.isAlive()) {
            createBattingScorecard(playing11Team1);
            createBowlingScorecard(bowler);
            taskThread = new Thread(() -> {
                while (enabled) {
                    try {
                        calculateScoreForFirstInnings();
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupted status
                        // Handle interruption
                    }
                }
            });
            taskThread.start();
        }
    }

    //Stop the thread of innings one.
    public void stopTask1() {
        enabled = false;
        if (taskThread != null) {
            taskThread.interrupt(); // Interrupt the thread if it is waiting or sleeping
            try {
                taskThread.join(); // Wait for the thread to finish execution
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                // Handle interruption
            }
        }
    }

    //Score calculation of the first innings.
    //Optimisation - Merge the score calculation in one method. Do only after the entire project is completed and have time.
    public void calculateScoreOfInnings2() throws InterruptedException {
        if (!enabled2) {
        } else {

            if (overs < 20 && wickets < 10 && target >= 0) {

                Object value = possibleOutcomes.get(RANDOM.nextInt(possibleOutcomes.size()));
                if (value instanceof Extra) {
                    extraScoreHandler((Extra) value);
                    target--;
                } else if (value instanceof String) {
                    if ("W".equals(value)) {
                        wicketHandler();
                    }
                } else if (value instanceof Integer) {
                    runsHandler((Integer) value);
                    target -= (Integer) value;
                }
                if (ballNumber % 6 == 0 && ballNumber != 0) {
                    overCompleteAndBowlerSelection();
                }
            } else {
                afterSecondInnings();
            }
        }
    }

    //Thread to start innings 2.
    public void startTask2() {
        createBattingScorecard(playing11Team2);
        createBowlingScorecard(bowler);
        new Thread(() -> {
            while (enabled2) {
                try {
                    calculateScoreOfInnings2();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    //Stop the thread of second innnings.
    public void stopTask2() {
        enabled2 = false;
        if (taskThread != null) {
            taskThread.interrupt();
            try {
                taskThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    //BallByBall display service to send the data based on number of innings completed.
    public Map<String, InningsDTO> getBallByBall(int mid){
        List<Innings> innings = inningsRepository.findByMid(mid);
        Map<String, InningsDTO> result = new HashMap<>();
        if(innings.isEmpty())
            return new HashMap<>();
        String battingTeamName = teamService.getById(innings.getFirst().getBattingId()).getTeamName();
        String bowlingTeamName = teamService.getById(innings.getFirst().getBowlingId()).getTeamName();
        InningsDTO inningsDTO1 = new InningsDTO(mid, battingTeamName, bowlingTeamName, ballByBallRepository.findByIid(innings.getFirst().getIid()));
        result.put("Innings1", inningsDTO1);
        if(innings.size()==1){
            return result;
        }
        battingTeamName = teamService.getById(innings.getLast().getBattingId()).getTeamName();
        bowlingTeamName = teamService.getById(innings.getLast().getBowlingId()).getTeamName();
        InningsDTO inningsDTO2 = new InningsDTO(mid, battingTeamName, bowlingTeamName, ballByBallRepository.findByIid(innings.getLast().getIid()));
        result.put("Innings2", inningsDTO2);
        return result;
    }
}