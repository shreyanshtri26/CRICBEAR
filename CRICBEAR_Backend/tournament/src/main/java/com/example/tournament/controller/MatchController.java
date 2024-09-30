package com.example.tournament.controller;

import com.example.tournament.model.DTO.MatchDTO;
import com.example.tournament.model.Match;
import com.example.tournament.model.MatchStatus;
import com.example.tournament.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/match")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class MatchController {
    @Autowired
    private MatchService matchService;

    //Generate the schedule of the tournament.
    @GetMapping("/schedule/{tid}/{uid}")
    public ResponseEntity<ArrayList<Match>> schedule(@PathVariable int tid, @PathVariable int uid) {
        try {
            ArrayList<Match> matches = matchService.scheduleMatches(tid,uid);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Generate the semi-final schedule.
    @GetMapping("/semi/{tid}/{uid}")
    public ResponseEntity<ArrayList<Match>> semiFinal(@PathVariable int tid, @PathVariable int uid) {
        try {
            ArrayList<Match> semiFinals = matchService.scheduleSemiFinal(tid,uid);
            return new ResponseEntity<>(semiFinals, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Start a match with match id.
    @GetMapping("/start/{mid}")
    public ResponseEntity<String> start(@PathVariable int mid) {
        try {
            String result = matchService.startMatch(mid);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Schedule the finals.
    @GetMapping("/final/{tid}/{uid}")
    public ResponseEntity<Match> finalMatch(@PathVariable int tid, @PathVariable int uid) {
        try {
            Match finalMatch = matchService.finalSchedule(tid,uid);
            return new ResponseEntity<>(finalMatch, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update only final and semi final.
    @GetMapping("/update/{mid}")
    public ResponseEntity<Match> update(@PathVariable int mid, @RequestBody Match match) {
        try{
            Match updatedMatch = matchService.updateMatchIfEligible(mid,match);
            return new ResponseEntity<>(updatedMatch, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get matches by status.
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MatchDTO>> status(@PathVariable MatchStatus status) {
        return new ResponseEntity<>(matchService.getMatchesByStatus(status),HttpStatus.OK);
    }

    //Get matches in a particular tournament.
    @GetMapping("/getByTid/{tid}")
    public ResponseEntity<List<MatchDTO>> getByTid(@PathVariable int tid) {
        return new ResponseEntity<>(matchService.findAllByTid(tid),HttpStatus.OK);
    }
}
