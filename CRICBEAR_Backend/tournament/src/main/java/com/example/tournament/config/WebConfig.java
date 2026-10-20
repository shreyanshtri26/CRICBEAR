package com.example.tournament.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Setting up CORS
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //CORS Mapping
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(System.getenv().getOrDefault("CORS_ORIGINS", "http://localhost:3000,http://localhost:3001").split(","))
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
