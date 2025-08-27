package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class home {
        @GetMapping("/")
        public String home() {
            return "Welcome to the Restaurant API!";
        }

}
