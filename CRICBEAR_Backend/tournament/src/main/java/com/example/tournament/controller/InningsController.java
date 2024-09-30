package com.example.tournament.controller;

import com.example.tournament.service.InningsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/innings")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class InningsController {

    @Autowired
    private InningsService inningsService;

    //Toss setup for a particular match with match id mid.
    @GetMapping("/{mid}")
    public void tossDecision(@PathVariable int mid) throws InterruptedException {
        inningsService.startMatchWithTossAndSetup(mid);
    }
}
