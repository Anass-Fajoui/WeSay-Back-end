package org.example.twitterproject.repositories;

import org.example.twitterproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUserName(String username);
    Optional<User> findByUserName(String username);
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUserNameContainingIgnoreCase(
            String firstName, String lastName, String userName);
}
