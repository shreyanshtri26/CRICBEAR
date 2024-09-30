package com.example.tournament.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//t.teamId, rt.groupNumber, t.points,t.nrr
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchSemiDTO {
    private Integer teamId;
    private Integer groupNumber;
    private Integer points;
    private Double nrr;
}