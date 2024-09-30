package com.example.tournament.model.DTO;

import com.example.tournament.model.TossDecision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResultDTO {
    int mid;
    TossDecision tossDecision;
    String tossWinnerName;
    String matchWinner;
    String team1Name;
    String team2Name;
    String battingFirst;
    String battingSecond;
    long runsScoredInFirstinnings;
    long wicketsInFirstinnings;
    long runsScoredInSecondinnings;
    long wicketsInSecondinnings;

}
