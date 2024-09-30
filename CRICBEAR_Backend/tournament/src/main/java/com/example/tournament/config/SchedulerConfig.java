package com.example.tournament.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//For threads
@Configuration
public class SchedulerConfig {

    //Provides a new thread
    @Bean
    public Thread thread() {
        return new Thread();
    }
}