package com.example.rating;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlternativeRatingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlternativeRatingApplication.class, args);
    }

}