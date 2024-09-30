package com.example.tournament.service;

import com.example.tournament.model.Status;
import com.example.tournament.model.Tournament;
import com.example.tournament.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;


    //Create a new Tournament
    public Tournament createTournament(Tournament tournament) {
        if (tournament.getTournamentName() == null || tournament.getTournamentName().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be null or empty.");
        }

        if (tournamentRepository.findByTournamentName(tournament.getTournamentName()) == null) {
            long duration = ChronoUnit.DAYS.between(tournament.getStartDate(), tournament.getEndDate());
            if (duration >= 12) {
                return tournamentRepository.save(tournament);
            }
            return null;
        }
        return null;
    }


    //Returns all tournaments in the database.
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }


    //Returns a tournament by id.
    public Tournament getById(Integer id) {
        return tournamentRepository.findByUid(id);
    }

    //Returns a tournament by the primary key tid.
    public Tournament getByTid(Integer tid) {
        return tournamentRepository.findByTid(tid);
    }


    //Update an existing tournament only if the uid is equal.
    public Tournament update(Tournament tournamentDetails,int uid) {
        if (tournamentDetails.getUid()!=uid)
            return null;
        Tournament extistingTournament = tournamentRepository.findByTid(tournamentDetails.getTid());
        extistingTournament.setTournamentName(tournamentDetails.getTournamentName());
        extistingTournament.setStartDate(tournamentDetails.getStartDate());
        extistingTournament.setEndDate(tournamentDetails.getEndDate());
        extistingTournament.setStatus(tournamentDetails.getStatus());
        return tournamentRepository.save(extistingTournament);
    }

    //Delete a tournament by id.
    public void delete(Integer id) {
        tournamentRepository.deleteById(id);
    }

    //Returns a list of tournaments with the status.
    public List<Tournament> getByStatus(String status) {
        return tournamentRepository.findAllByStatus(Status.valueOf(status));
    }

    //Start a tournament if the date is equal to current date.
    public boolean start(int tid, int uid){
        Tournament tournament = tournamentRepository.findByTid(tid);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);
        if(tournament.getUid() == uid && tournament.getStartDate().format(formatter).equals(formattedDateTime)){
            tournament.setStatus(Status.LIVE);
            update(tournament,uid);
            return true;
        }
        else {
            return false;
        }
    }
}
