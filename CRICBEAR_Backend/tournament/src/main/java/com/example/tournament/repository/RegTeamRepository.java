package com.example.tournament.repository;

import com.example.tournament.model.RegTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface RegTeamRepository extends JpaRepository<RegTeam, Integer> {
    //Find number of teams registered in a tournament.
    @Query("select count(*) from RegTeam where tid = :tid")
    int countByTid(int tid);

    //Find teams by group number in a particular tournament.
    @Query("select r from RegTeam r where r.groupNumber = :groupNumber and r.tid = :tid")
    ArrayList<RegTeam> findByGroupNumber(int groupNumber, int tid);

    //Find a team by tournament and team id.
    @Query("select rt from RegTeam rt where rt.tid = :tid and rt.teamid = :teamId")
    RegTeam findByTidAndTeamId(int tid,int teamId);
}
