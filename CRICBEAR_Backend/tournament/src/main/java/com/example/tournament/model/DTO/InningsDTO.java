package com.example.tournament.model.DTO;

import com.example.tournament.model.BallByBall;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InningsDTO {
    int mid;
    String battingTeamName;
    String bowlingTeamName;
    List<BallByBall> ballByBall;
}
