package com.example.demo.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.security.Key;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public Key jwtSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Bean
    public JwtParser jwtParser(Key jwtSigningKey) {
        return Jwts.parser()
                .verifyWith((SecretKey) jwtSigningKey)
                .build();
    }
}