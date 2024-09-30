package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//To store the Team details and the statistics.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team {

    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamid")
    private Integer teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "coachid")
    private Integer coachId;

    @Column(name = "matches_played")
    private Long matchesPlayed;

    @Column(name = "matches_won")
    private Long matchesWon;

    @Column(name = "matches_lost")
    private Long matchesLost;

    @Column(name = "matches_drawn")
    private Long matchesDrawn;

    @Column(name = "matches_abandoned")
    private Long matchesAbandoned;

    @Column(name = "points")
    private Integer points;

    @Column(name = "nrr")
    private Double nrr;

    public Team(String teamName, Integer coachId, Long matchesPlayed,Long matchesWon, Long matchesLost, Long matchesDrawn, Long matchesAbandoned, Double nrr ) {
        this.teamName = teamName;
        this.coachId = coachId;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesLost = matchesLost;
        this.matchesDrawn = matchesDrawn;
        this.matchesAbandoned = matchesAbandoned;
        this.nrr = nrr;
    }

}
