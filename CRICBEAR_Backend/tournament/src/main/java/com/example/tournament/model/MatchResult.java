package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "match_result")
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mrid")
    private int mrid;

    @Column(name = "mid")
    private int mid;

    @Column(name = "toss_desicion")
    @Enumerated(EnumType.STRING)
    private TossDecision tossDesicion;

    @Column(name = "toss_win_team")
    private int tossWinTeam;

    @Column(name = "winner_id")
    private int winnerId;

    public MatchResult(int mid, TossDecision tossDecision, int tossWinTeam, int teamId) {
        this.mid = mid;
        this.tossDesicion = tossDecision;
        this.tossWinTeam = tossWinTeam;
        this.winnerId = teamId;
    }
}
