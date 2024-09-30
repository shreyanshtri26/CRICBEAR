package com.example.tournament.repository;

import com.example.tournament.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    //Find a player by name.
    Player findByName(String name);

    //Find by player id.
    Player findByPid(int pid);

    //Delete player by name.
    void deleteByName(String name);

}