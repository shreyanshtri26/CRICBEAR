package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.YamlProcessor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mid")
    private int mid;

    @Column(name = "teamid_1")
    private int teamId1;

    @Column(name = "teamid_2")
    private int teamId2;

    @Column(name = "match_date")
    private LocalDateTime matchDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @Column(name = "stadium")
    private String stadium;

    @Column(name = "tid")
    private int tid;

    @Column(name = "match_type")
    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    public Match(int teamId1,int teamId2,LocalDateTime matchDate,MatchStatus status,String stadium,int tid,MatchType matchType) {
        this.teamId1 = teamId1;
        this.teamId2 = teamId2;
        this.matchDate = matchDate;
        this.status = status;
        this.stadium = stadium;
        this.tid = tid;
        this.matchType = matchType;
    }
}
