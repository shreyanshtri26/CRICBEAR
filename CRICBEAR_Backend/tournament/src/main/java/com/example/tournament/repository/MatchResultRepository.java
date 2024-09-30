package com.example.tournament.repository;

import com.example.tournament.model.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Integer> {
    //Find match result by match id.
    MatchResult findByMid(Integer matchId);
}
