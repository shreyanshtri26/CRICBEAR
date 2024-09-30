package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/user/**")
                        .uri("http://localhost:8083/user"))
                .route(r -> r.path("/notifications/**")
                        .uri("http://localhost:8081/notifications"))
                .route(r -> r.path("/tournament/**")
                        .uri("http://localhost:8084/tournament"))
                .build();
    }
}
