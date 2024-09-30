package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "player_profile")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private int pid;


    @Column(name = "name")
    private String name;


    @Column(name = "overseas")
    private boolean overseas;


    @Column(name = "player_roles")
    @Enumerated(EnumType.STRING)
    private PlayerRole playerRole;

    @Column(name = "flag")
    private boolean flag;


    // Constructor with all fields except ID
    public Player(String name, boolean overseas, PlayerRole playerRole) {
        this.name = name;
        this.overseas = overseas;
        this.playerRole = playerRole;
    }


}