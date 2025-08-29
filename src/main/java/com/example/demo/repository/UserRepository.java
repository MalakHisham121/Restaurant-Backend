package com.example.demo.repository;

import com.example.demo.dto.UserSummerDTO;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u.username AS username, u.role AS role FROM User u")
    List<UserSummerDTO> findAllUsernamesAndRoles();

    @Query("SELECT username from User where username = :userName ")
    Optional<User> findByUserName(String userName);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean existsByUsername( String username);




}
