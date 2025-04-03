package org.example.twitterproject.repositories;

import org.example.twitterproject.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepo extends JpaRepository<Tweet, Integer> {

}
