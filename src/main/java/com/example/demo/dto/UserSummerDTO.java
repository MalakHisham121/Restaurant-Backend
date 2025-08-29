package com.example.demo.dto;

public class UserSummerDTO {
    private final String username;
    private final String role;

    // Constructor for JPQL
    public UserSummerDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
