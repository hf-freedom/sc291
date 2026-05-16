package com.visitor.access;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VisitorAccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisitorAccessApplication.class, args);
    }
}
