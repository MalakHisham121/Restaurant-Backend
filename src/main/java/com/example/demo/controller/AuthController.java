package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
import org.apache.commons.logging.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username,
                                           @RequestParam String email,
                                           @RequestParam String password,
                                           @RequestParam Role role) {
        authService.register(username, email, password, role);
        return ResponseEntity.ok(String.format("User %s registered successfully", username));
    }

    @PostMapping("/login")
    public ResponseEntity<?>  login(@RequestParam String username,
                                @RequestParam String password) {
        String token = authService.login(username, password);
        return ResponseEntity.ok(String.format("Bearer " + token));
    }
}



