package com.example.tournament.repository;

import com.example.tournament.model.Innings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InningsRepository extends JpaRepository<Innings, Integer> {
    //Find the innings by match id.
    List<Innings> findByMid(Integer mid);

    //To get a single innings.
    Innings getByMid(Integer mid);

    //Find by innings id.
    Innings getByIid(int iid);
}
