package com.example.demo.dto;

import com.example.demo.entity.Role;

public class UserSummerDTO {
    private final String username;
    private final String role;

    // Constructor for JPQL with String role
    public UserSummerDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Constructor for JPQL with Role enum
    public UserSummerDTO(String username, Role role) {
        this.username = username;
        this.role = role.toString();
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
