package com.example.tournament.controller;

import com.example.tournament.model.BattingScore;
import com.example.tournament.model.BowlingScore;
import com.example.tournament.service.BattingScoreService;
import com.example.tournament.service.BowlingScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bowlingScore")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class BowlingScoreController {

    @Autowired
    private BowlingScoreService bowlingScoreService;

    //Get the bowling scorecard for a match.
    @GetMapping("/{mid}")
    public ResponseEntity<?> getBallByBall(@PathVariable Integer mid) {
        try {
            Map<String, List<BowlingScore>> bowlingScoresData = bowlingScoreService.getAllDataByMid(mid);

            if (bowlingScoresData == null || bowlingScoresData.isEmpty()) {
                return new ResponseEntity<>("No data found for the provided match ID", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bowlingScoresData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while retrieving data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}