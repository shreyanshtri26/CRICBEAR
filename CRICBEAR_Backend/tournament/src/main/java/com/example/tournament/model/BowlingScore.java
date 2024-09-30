package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bowling_score")
public class BowlingScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bsid;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "runs_given", columnDefinition = "bigint default 0")
    private Long runsGiven;

    @Column(name = "overs", columnDefinition = "int default 0")
    private Integer overs;

    @Column(name = "wickets")
    private Integer wickets;

    @Column(name = "iid")
    private Integer iid;

    public BowlingScore(String bs, Long runsGiven, int overs, int wickets,Integer iid) {
        this.playerName = bs;
        this.runsGiven = runsGiven;
        this.overs = overs;
        this.wickets = wickets;
        this.iid = iid;
    }
}
