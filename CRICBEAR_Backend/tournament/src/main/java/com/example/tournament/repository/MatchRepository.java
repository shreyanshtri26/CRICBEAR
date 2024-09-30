package com.example.tournament.repository;

import com.example.tournament.model.Match;
import com.example.tournament.model.MatchStatus;
import com.example.tournament.model.MatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    //Find all matches in a tournament.
    List<Match> findAllByTid(int tournamentId);

    //Find matches by the type.
    List<Match> findByMatchType(MatchType matchType);

    //Get a match details by the match id.
    Match getByMid(Integer mid);

    //Find teams eligible for semi final in descending order of points..
    @Query("select t.teamId, rt.groupNumber, t.points,t.nrr from RegTeam rt join Team t on rt.teamid = t.teamId where rt.tid =:tid and rt.groupNumber = :groupNumber order by t.points desc")
    List<Object[]> getSemiFinal(int tid, int groupNumber);

    //Get a match in a tournament and of match type.
    @Query("select m from Match m where m.tid = :tid and m.matchType = :matchType")
    List<Match> getByTidAndMatchType(Integer tid, MatchType matchType);

    //Get matches by match status.
    @Query("select m from Match m where m.status = :status")
    List<Match> getByStatus(MatchStatus status);
}
