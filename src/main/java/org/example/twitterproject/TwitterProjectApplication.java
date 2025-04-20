package org.example.twitterproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterProjectApplication implements CommandLineRunner {

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    public static void main(String[] args) {
        SpringApplication.run(TwitterProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("JWT secret is " + jwtSecret);

    }
}
