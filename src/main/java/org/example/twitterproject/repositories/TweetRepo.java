package org.example.twitterproject.repositories;

import org.example.twitterproject.models.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepo extends JpaRepository<Tweet, Integer> {

    Page<Tweet> findAll(Pageable pageable);

    Page<Tweet> findByWriterId(int id, Pageable pageable);
}
