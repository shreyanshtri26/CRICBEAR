package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ballbyball")
public class BallByBall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid")
    private int bid;

    @Column(name = "iid")
    private int iid;

    @Column(name = "over_number")
    private int overNumber;

    @Column(name = "ball_number")
    private int ballNumber;

    @Column(name = "batsmen1")
    private String batsmen1;

    @Column(name = "batsmen2")
    private String batsmen2;

    @Column(name = "bowler")
    private String bowler;

    @Column(name = "runs_scored")
    private Integer runsScored;

    @Column(name = "extra")
    @Enumerated(EnumType.STRING)
    private Extra extra;

    @Column(name = "wicket")
    @Enumerated(EnumType.STRING)
    private Wicket wicket;

    @Column(name = "total")
    private Integer total;

    @Column(name = "wicket_number")
    private int wicketNumber;

    public BallByBall(int iid, int overs, int ballNumber, String batsman1Id, String batsman2Id, String bowlerid, Integer integer, Extra extra, Wicket wicket, int runs, int wickets) {
        this.iid = iid;
        this.overNumber = overs;
        this.ballNumber = ballNumber;
        this.batsmen1 = batsman1Id;
        this.batsmen2 = batsman2Id;
        this.bowler = bowlerid;
        this.runsScored = integer;
        this.extra = extra;
        this.wicket = wicket;
        total = Math.toIntExact(runs);
        this.total = runs;
        this.wicketNumber = wickets;
    }
}
