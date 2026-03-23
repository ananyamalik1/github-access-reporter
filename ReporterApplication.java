package com.cloudeagle.reporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReporterApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ReporterApplication.class, args);
        System.out.println("✅ GitHub Access Reporter Started!");
    }
}