package org.example.twitterproject.repositories;

import org.example.twitterproject.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepo extends JpaRepository<Tweet, Integer> {

}
