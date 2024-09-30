package com.example.tournament.service;

import com.example.tournament.model.*;
import com.example.tournament.model.DTO.MatchDTO;
import com.example.tournament.model.DTO.MatchSemiDTO;
import com.example.tournament.model.DTO.MatchSemiDTOComparator;
import com.example.tournament.model.DTO.MatchSemiDTOConverter;
import com.example.tournament.repository.MatchRepository;
import com.example.tournament.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private RegTeamService regTeamService;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    InningsService inningsService;

    @Autowired
    private MatchResultService matchResultService;

    @Autowired
    private TeamRepository teamRepository;

    private static final String STADIUM = "Chinnaswamy";

    //Return all matches in the database.
    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    //Find a match by the id.
    public Match findById(Integer id) {
        return matchRepository.findById(id).get();
    }

    //Save the match details.
    public Match save(Match match) {
        return matchRepository.save(match);
    }

    //Delete a match.
    public void delete(Match match) {
        matchRepository.delete(match);
    }

    //Find matches in a tournament and return in the DTO format with names of the teams.
    public List<MatchDTO> findAllByTid(Integer tid) {
        List<Match> matches = matchRepository.findAllByTid(tid);
        List<MatchDTO> matchDTOs = new ArrayList<>();
        for (Match match : matches) {
            matchDTOs.add(new MatchDTO(match.getMid(),
                    match.getTid(),
                    teamRepository.findByTeamId(match.getTeamId1()).getTeamName(),
                    teamRepository.findByTeamId(match.getTeamId2()).getTeamName(),
                    match.getMatchDate(),
                    match.getStadium(),
                    match.getStatus(),
                    match.getMatchType()));
        }
        return matchDTOs;
    }

    //Find matches by the type - NORMAL, SEMIFINAL, FINAL.
    public List<Match> findByMatchType(MatchType matchType){
        return matchRepository.findByMatchType(matchType);
    }

    //Update a match.
    public Match update(Match match) {
        return matchRepository.save(match);
    }

    //Starts a match if it is scheduled to be played today and updates its status to 'LIVE'.
    public String startMatch(int mid) throws InterruptedException {
        Match match = matchRepository.getByMid(mid);
        if(match != null && match.getStatus().equals(MatchStatus.UPCOMING)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateTime = now.format(formatter);
            if (formattedDateTime.equals(formatter.format(match.getMatchDate()))){
                match.setStatus(MatchStatus.LIVE);
                matchRepository.save(match);
                inningsService.startMatchWithTossAndSetup(mid);
                return "Match started";
            }
        }
        return null;
    }

    //Schedules matches for the given tournament and user ID by creating matches between teams in two groups.
    public ArrayList<Match> scheduleMatches(Integer tid,int uid) {
        Tournament tournament = tournamentService.getByTid(tid);
        if(tournament.getUid()!=uid)
            return new ArrayList<>();
        ArrayList<Match> matches = new ArrayList<>();
        ArrayList<RegTeam> group1 = regTeamService.findByGroupNumber(1,tid);
        ArrayList<RegTeam> group2 = regTeamService.findByGroupNumber(2,tid);

        int j = 0;
        int daysToAdd = 0;
        int i = 1;
        while (j < group1.size() && i < group1.size()) {
            if (i != j) {
                Match match = new Match(
                        group1.get(j).getTeamid(),
                        group1.get(i).getTeamid(),
                        tournament.getStartDate().toLocalDate().plusDays(daysToAdd).atStartOfDay(),
                        MatchStatus.UPCOMING,
                        STADIUM,
                        tid,
                        MatchType.NORMAL
                );

                matches.add(match);
                daysToAdd++;

                Match match2 = new Match(
                        group2.get(j).getTeamid(),
                        group2.get(i).getTeamid(),
                        tournament.getStartDate().toLocalDate().plusDays(daysToAdd).atStartOfDay(),
                        MatchStatus.UPCOMING,
                        STADIUM,
                        tid,
                        MatchType.NORMAL
                );
                matches.add(match2);
                daysToAdd++;
                matchRepository.save(match);
                matchRepository.save(match2);
            }
            if (i == group1.size() - 1) {
                i = 1;
                j++;
            } else {
                i++;
            }
        }
        return matches;
    }

    //Schedules semi-final matches for a given tournament and user ID.
    public ArrayList<Match> scheduleSemiFinal(Integer tid,Integer uid) {
        Tournament tournament = tournamentService.getByTid(tid);
        if(tournament.getUid()!=uid)
            return new ArrayList<>();
        ArrayList<Match> semiMatches = new ArrayList<>();
        List<Object[]> results1 = matchRepository.getSemiFinal(tid,1);
        List<Object[]> results2 = matchRepository.getSemiFinal(tid,2);
        ArrayList<MatchSemiDTO> matchDTO1 = new ArrayList<>();
        ArrayList<MatchSemiDTO> matchDTO2 = new ArrayList<>();

        for (Object[] result : results1) {
            MatchSemiDTO matchDTO = MatchSemiDTOConverter.convert(result);
            matchDTO1.add(matchDTO);
        }
        for (Object[] result : results2) {
            MatchSemiDTO matchDTO = MatchSemiDTOConverter.convert(result);
            matchDTO2.add(matchDTO);
        }

        Collections.sort(matchDTO1, new MatchSemiDTOComparator());
        Collections.sort(matchDTO2, new MatchSemiDTOComparator());
        Match match = new Match(matchDTO1.get(0).getTeamId(),matchDTO2.get(1).getTeamId(),LocalDateTime.now(),MatchStatus.UPCOMING,STADIUM,tid,MatchType.SEMIFINAL);
        semiMatches.add(match);
        Match match2 = new Match(matchDTO1.get(1).getTeamId(),matchDTO2.get(0).getTeamId(),LocalDateTime.now(),MatchStatus.UPCOMING,STADIUM,tid,MatchType.SEMIFINAL);
        semiMatches.add(match2);
        matchRepository.save(match);
        matchRepository.save(match2);
        return semiMatches;
    }


    //Schedules the final match for a given tournament based on the results of the semi-final matches.
    public Match finalSchedule(int tid,int uid){
        Tournament tournament = tournamentService.getByTid(tid);
        if(tournament.getUid()!=uid)
            return null;
        List<Match> semiFinalMatches = matchRepository.getByTidAndMatchType(tid,MatchType.SEMIFINAL);
        if(semiFinalMatches.size()<2)
            return null;
        MatchResult semiFinalMatch1 = matchResultService.getMatchResultByMatchId(semiFinalMatches.get(0).getMid());
        MatchResult semiFinalMatch2 = matchResultService.getMatchResultByMatchId(semiFinalMatches.get(1).getMid());
        Match finalMatch = new Match(semiFinalMatch1.getWinnerId(),semiFinalMatch2.getWinnerId(),LocalDateTime.now(),MatchStatus.UPCOMING,STADIUM,tid,MatchType.FINAL);
        matchRepository.save(finalMatch);
        return finalMatch;
    }

    //Updates a match if it is a semi-final or final and the new match date is at least 1 day after the original match date.
    public Match updateMatchIfEligible(int mid, Match match) {
        if (match.getMatchType().equals(MatchType.SEMIFINAL) || match.getMatchType().equals(MatchType.FINAL)) {
            Match beforeUpdate = matchRepository.getByMid(mid);
            if(ChronoUnit.DAYS.between(beforeUpdate.getMatchDate(),match.getMatchDate())<2){
                return matchRepository.save(match);
            }
        }
        return null;
    }

    //Returns a list of matches of a particular status with the team names.
    public List<MatchDTO> getMatchesByStatus(MatchStatus status) {
        List<Match> matches = matchRepository.getByStatus(status);
        List<MatchDTO> matchDTOs = new ArrayList<>();
        for (Match match : matches) {
            String teamName1 = teamRepository.findByTeamId(match.getTeamId1()).getTeamName();
            String teamName2 = teamRepository.findByTeamId(match.getTeamId2()).getTeamName();

            MatchDTO matchDTO = new MatchDTO(
                    match.getMid(),
                    match.getTid(),
                    teamName1,
                    teamName2,
                    match.getMatchDate(),
                    match.getStadium(),
                    status,
                    match.getMatchType()
            );
            matchDTOs.add(matchDTO);
        }
        return matchDTOs;
    }
}