package com.example.tournament.repository;

import com.example.tournament.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    //Find by team name.
    Team findByTeamName(String name);

    //Find by team id.
    Team findByTeamId(Integer id);

    //Find teams in descending order of points.
    @Query("select t from Team t order by t.points desc")
    List<Team> findAllTeamsInDescendingOrder();

    //Find team by a coach.
    Team findByCoachId(int coachid);
}
