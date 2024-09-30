package com.example.tournament.controller;

import com.example.tournament.model.Team;
import com.example.tournament.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class TeamController {

    @Autowired
    private TeamService teamService;

    //Create a new team.
    @PostMapping("/create")
    public ResponseEntity<Team> create(@RequestBody Team team) {
        Team createdTeam = teamService.createTeam(team);
        return new ResponseEntity<>(createdTeam, HttpStatus.CREATED);
    }

    //Update a team.
    @PutMapping("/update")
    public ResponseEntity<Team> update(@RequestBody Team team) {
        Team updatedTeam = teamService.update(team.getTeamId(), team);
        if (updatedTeam != null) {
            return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    //Get a team by its coachid.
    @GetMapping("/getTeamId/{coachid}")
    public ResponseEntity<Team> getTeamId(@PathVariable int coachid) {
        Team team = teamService.getByCoachId(coachid);
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    //Get a team by its name.
    @GetMapping("/getByName/{name}")
    public ResponseEntity<Team> getByTeamName(@PathVariable String name) {
        Team team = teamService.getByTeamName(name);
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    //Get all teams.
    @GetMapping("/getAll")
    public ResponseEntity<List<Team>> getAll() {
        List<Team> teams = teamService.getAll();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    //Get by teamid.
    @GetMapping("/getByID/{id}")
    public ResponseEntity<Team> getByID(@PathVariable int id) {
        Team team = teamService.getById(id);
        if (team != null) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    //Delete a team.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        teamService.delete(id);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    //Sort the teams in descending order of points.
    @GetMapping("/findInDesc")
    public ResponseEntity<List<Team>> findInDesc() {
        return new ResponseEntity<>(teamService.findAllTeamsInDescendingOrder(), HttpStatus.OK);
    }
}
