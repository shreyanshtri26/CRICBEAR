package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "batting_score")
public class BattingScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bsid;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "runs_scored", columnDefinition = "bigint default 0")
    private Long runsScored;

    @Column(name = "balls_faced", columnDefinition = "int default 0")
    private Integer ballsFaced;

    @Column(name = "fours")
    private Integer fours;

    @Column(name = "six")
    private Integer six;

    @Column(name = "is_out", columnDefinition = "boolean default false")
    private Boolean isOut;

    @Column(name = "iid")
    private Integer iid;

    public BattingScore(String bs, Long runsScored, int ballsFaced,int fours, int six, boolean isOut, Integer iid) {
        this.playerName = bs;
        this.runsScored = runsScored;
        this.ballsFaced = ballsFaced;
        this.fours = fours;
        this.six = six;
        this.isOut = isOut;
        this.iid = iid;
    }
}
