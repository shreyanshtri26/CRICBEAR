package com.example.tournament.repository;

import com.example.tournament.model.BattingScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattingScoreRepository extends JpaRepository<BattingScore, Integer> {

    //Find by player name and innings id.
    @Query("select bs from BattingScore bs where bs.playerName = :playerName and bs.iid = :iid")
    BattingScore findByNameAndIid(String playerName, Integer iid);

    //Find by innings id.
    List<BattingScore> findByIid(Integer iid);
}
