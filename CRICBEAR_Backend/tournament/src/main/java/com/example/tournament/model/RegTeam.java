package com.example.tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Represents a registered team in a tournament.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reg_team")
public class RegTeam {

    //Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rtid")
    private Integer rtid;

    @Column(name = "tid")
    private Integer tid;

    @Column(name = "teamid")
    private Integer teamid;

    @Column(name = "group_number")
    private Integer groupNumber;

    public RegTeam(Integer tid, Integer teamid) {
        this.tid = tid;
        this.teamid = teamid;
    }
}
