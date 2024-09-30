package com.example.tournament.repository;

import com.example.tournament.model.PlayerRole;
import com.example.tournament.model.PlayerTeam;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerTeamRepository extends JpaRepository<PlayerTeam, Integer> {
    //Find a player by team id.
    @Query("SELECT t.teamId,p.pid ,t.teamName,p.name ,p.overseas,p.playerRole, pt.runsScored, pt.balls,pt.wickets,pt.overs,pt.runsGiven FROM PlayerTeam pt JOIN Team t on pt.teamId = t.teamId, Player p WHERE p.pid = pt.pid and t.teamId = :teamId")
    List<Object[]> findPlayerTeamByTeamId(@Param("teamId") int teamId);

    //Find a player of a particular role and in a team with limit.
    @Query(value = "SELECT p.name FROM team_player pt JOIN player_profile p on pt.pid = p.pid WHERE pt.teamId = :teamId AND pt.player_roles = :role LIMIT :limit",nativeQuery = true)
    List<String> findByRoleAndTeam(@Param("teamId") Integer teamId, @Param("role") String role, @Param("limit") int limit);

    //Count the number of overseas players in a team.
    @Query("select count(*) from PlayerTeam pt where pt.teamId = :teamId and pt.overseas=true")
    Long countByOverseas(int teamId);

    //Count players in a team.
    @Query("select count(*) from PlayerTeam pt where pt.teamId = :teamId")
    Integer countByTeamId(int teamId);

    @Transactional
    int deleteByPid(int pid);

    PlayerTeam findByPid(int pid);
}