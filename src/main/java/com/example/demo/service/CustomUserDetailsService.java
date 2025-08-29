package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.dto.UserSummerDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public List<UserSummerDTO> listAllUsers(){
         return userRepository.findAllUsernamesAndRoles();
    }

    @Transactional
    public void deleteUser(Long userId) {
        // Validate input
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new UsernameNotFoundException("No user found with ID: " + userId);
        }

        // Delete user
        userRepository.deleteById(userId);
    }


    @Transactional
    public void updateUser(Long userId, UserDTO userDTO) {
        // Validate input
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (userDTO == null) {
            throw new IllegalArgumentException("User data cannot be null");
        }

        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with ID: " + userId));

        // Check for username uniqueness (excluding current user)
        if (!user.getUsername().equals(userDTO.getUsername()) &&
                userRepository.existsByUsername(
                        userDTO.getUsername())) {
            throw new IllegalArgumentException("Username '" + userDTO.getUsername() + "' is already taken");
        }

        // Update fields
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());
        user.setUpdatedAt(OffsetDateTime.now());


        userRepository.save(user);
    }
}
