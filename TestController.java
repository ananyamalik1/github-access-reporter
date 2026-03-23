package com.cloudeagle.reporter.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "✅ GitHub Access Reporter is Working!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}