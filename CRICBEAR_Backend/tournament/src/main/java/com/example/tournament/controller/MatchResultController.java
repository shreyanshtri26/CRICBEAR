package com.example.tournament.controller;

import com.example.tournament.service.MatchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matchResult")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class MatchResultController {
    @Autowired
    private MatchResultService matchResultService;

    //Get the match results by the id.
    @GetMapping("/getResultById/{mid}")
    public ResponseEntity<?> getByMatchId(@PathVariable Integer mid) {
        return ResponseEntity.ok(matchResultService.getResultByMatchId(mid));
    }
}
