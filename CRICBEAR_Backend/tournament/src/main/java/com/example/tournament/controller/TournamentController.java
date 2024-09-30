package com.example.tournament.controller;

import com.example.tournament.model.Match;
import com.example.tournament.model.Tournament;
import com.example.tournament.service.MatchService;
import com.example.tournament.service.TournamentService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tournament")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private MatchService matchService;

    //To create a new tournament.
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Tournament tournament) {
        if (tournament.getTournamentName() == null || tournament.getTournamentName().isEmpty()) {
            return ResponseEntity.badRequest().body("Tournament name cannot be null or empty.");
        }

        Tournament createdTournament = tournamentService.createTournament(tournament);
        return ResponseEntity.ok(createdTournament);
    }

    //Update a Tournament.
    @PutMapping("/update/{uid}")
    public ResponseEntity<Tournament> update(@RequestBody Tournament tournament,@PathVariable int uid) {
        Tournament updatedTournament = tournamentService.update(tournament,uid);
        if (updatedTournament != null) {
            return new ResponseEntity<>(updatedTournament, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    //Get a tournament by id.
    @GetMapping("/getById/{id}")
    public ResponseEntity<Tournament> getById(@PathVariable Integer id) {
        Tournament tournament = tournamentService.getById(id);
        if (tournament != null) {
            return new ResponseEntity<>(tournament, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    //Get all tournaments.
    @GetMapping("/getAll")
    public ResponseEntity<List<Tournament>> getAll() {
        List<Tournament> tournaments = tournamentService.getAll();
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }

    //Delete a Tournament.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        tournamentService.delete(id);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    //Get by LIVE,UPCOMING,COMPLETED
    @PostMapping("/getByStatus")
    public ResponseEntity<List<Tournament>> getByStatus(@RequestBody Map<String, String> status) {
        List<Tournament> allTournaments = tournamentService.getByStatus(status.get("status"));
        if (allTournaments.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(allTournaments, HttpStatus.OK);
        }
    }

    //Start a tournament and return list of scheduled matches.
    @PostMapping("/start")
    public ResponseEntity<ArrayList<Match>> start(@RequestBody Map<String, Integer> tournament) {
        boolean success = tournamentService.start(tournament.get("tid"), tournament.get("uid"));
        if (success) {
            return new ResponseEntity<>(matchService.scheduleMatches(tournament.get("tid"), tournament.get("uid")), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    //Get by tid.
    @GetMapping("/getByTid/{tid}")
    public ResponseEntity<Tournament> getByTid(@PathVariable Integer tid) {
        Tournament tournament = tournamentService.getByTid(tid);
        if (tournament != null) {
            return new ResponseEntity<>(tournament, HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
}
