package com.example.tournament.repository;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    //Find by Tournament id.
    Tournament findByTid(int id);

    //Find by tournament name.
    Tournament findByTournamentName(String name);

    //Find the tournaments owned by a user.
    Tournament findByUid(int id);

    //Find the tournaments by status.
    List<Tournament> findAllByStatus(Status status);
}