package com.example.gateway;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001"})
public class GatewayController {
    @GetMapping("/")
    public String index() {
        return "Hello World";
    }
}
