package com.example.demo;
import io.github.cdimascio.dotenv.Dotenv;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
        System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
        System.setProperty("SPRING_DATASOURCE_URL", Objects.requireNonNull(dotenv.get("SPRING_DATASOURCE_URL")));
        System.setProperty("JWT_SECRET", Objects.requireNonNull(dotenv.get("JWT_SECRET")));
        System.setProperty("JWT_EXPIRATION", Objects.requireNonNull(dotenv.get("JWT_EXPIRATION")));

        SpringApplication.run(DemoApplication.class, args);
    }

}
