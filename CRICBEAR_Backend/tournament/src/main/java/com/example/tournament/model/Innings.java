package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "innings")
public class Innings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iid")
    private int iid;

    @Column(name = "mid")
    private int mid;

    @Column(name = "batting_id")
    private int battingId;

    @Column(name = "bowling_id")
    private int bowlingId;

    @Column(name = "runs")
    private long runs;

    @Column(name = "wickets")
    private int wickets;

    @Column(name = "overs")
    private double overs;

    public Innings(Integer mid, int battingId, int bowlingId) {
        this.mid = mid;
        this.battingId = battingId;
        this.bowlingId = bowlingId;
    }

    public Innings(int iid, int runs, int wickets, double overToSave) {
        this.iid = iid;
        this.runs = runs;
        this.wickets = wickets;
        this.overs = overToSave;
    }

    public Innings(int iid, int mid, int runs, int wickets, double overToSave) {
        this.iid = iid;
        this.mid = mid;
        this.runs = runs;
        this.wickets = wickets;
        this.overs = overToSave;
    }
}
