package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
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
    public User register(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String role) {
        return authService.register(username, password, role);
    }

    @PostMapping("/login")
    public Authentication login(@RequestParam String username,
                                @RequestParam String password) {
        return authService.login(username, password);
    }
}
