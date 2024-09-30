package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team_player")
public class PlayerTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tpid")
    private Integer tpid;

    @Column(name = "pid")
    private Integer pid;

    @Column(name = "teamid")
    private Integer teamId;

    @Column(name = "overseas")
    private boolean overseas;


    @Column(name = "player_roles")
    @Enumerated(EnumType.STRING)
    private PlayerRole playerRole;


    @Column(name = "runs_scored")
    private Integer runsScored;

    @Column(name = "balls")
    private Integer balls;

    @Column(name = "wickets")
    private Integer wickets;

    @Column(name = "overs")
    private double overs;

    @Column(name = "runs_given")
    private Integer runsGiven;

    @Column(name = "flag")
    private boolean flag;

}
