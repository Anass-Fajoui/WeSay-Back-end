package org.example.twitterproject.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.twitterproject.Exceptions.EntityNotFoundException;
import org.example.twitterproject.config.JwtService;
import org.example.twitterproject.models.Tweet;
import org.example.twitterproject.models.TweetDTO;
import org.example.twitterproject.models.User;
import org.example.twitterproject.repositories.TweetRepo;
import org.example.twitterproject.repositories.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class TweetService {

    @Autowired
    private TweetRepo tweetRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    public Page<Tweet> getTweets(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return tweetRepo.findAll(pageable);
    }

    public Tweet getTweet(int id) {
        Tweet tweet = tweetRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tweet with id "+id+" is not found"));
        return tweet;
    }

    public Page<Tweet> getTweetsByUser(int id, int page, int size) {
        if (!userRepo.existsById(id)){
            throw new EntityNotFoundException("User with id : "+id+" is not found");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return tweetRepo.findByWriterId(id, pageable);
    }

    public void addTweet(TweetDTO tweetD, String token) {
        int tokenId = jwtService.extractId(token);

        User writer = userRepo.findById(tokenId)
                .orElseThrow(() -> new EntityNotFoundException("User "+tokenId+"in token not found"));

        Tweet tweet = new Tweet();
        tweet.setTitle(tweetD.getTitle());
        tweet.setContent(tweetD.getContent());
        tweet.setCreatedAt(LocalDateTime.now());
        tweet.setLastModifiedAt(LocalDateTime.now());
        tweet.setWriter(writer);

//        writer.addTweet(tweet);
        tweetRepo.save(tweet);
    }

    public void updateTweet(TweetDTO tweetd, int id, String token) {
        int tokenId = jwtService.extractId(token);
        if (!userRepo.existsById(tokenId)){
            throw new EntityNotFoundException("User "+tokenId+" in token not found");
        }
        Tweet currentTweet = tweetRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tweet "+id+" Not Found"));

        if (tokenId != currentTweet.getWriter().getId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
        }

        currentTweet.setTitle(tweetd.getTitle());
        currentTweet.setContent(tweetd.getContent());
        currentTweet.setLastModifiedAt(LocalDateTime.now());

//        tokenUser.updateTweet(currentTweet);
        tweetRepo.save(currentTweet);
    }

    public void deleteTweet(int id, String token) {
        int tokenId = jwtService.extractId(token);
        if (!userRepo.existsById(tokenId)){
            throw new EntityNotFoundException("User "+tokenId+" in token not found");
        }
        Tweet currentTweet = tweetRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tweet "+id+" Not Found"));

        if (tokenId != currentTweet.getWriter().getId()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
        }

//        tokenUser.removeTweet(currentTweet);
        tweetRepo.deleteById(id);
    }

    public int like(int id, String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User '"+username+"' not found"));

        Tweet currentTweet = tweetRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tweet "+id+" Not Found"));

        currentTweet.like(user);
        tweetRepo.save(currentTweet);
        userRepo.save(user);
        return currentTweet.getLikes();
    }

    public int unlike(int id, String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("User '"+username+"' not found"));

        Tweet currentTweet = tweetRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tweet "+id+" Not Found"));

        currentTweet.unlike(user);
        tweetRepo.save(currentTweet);
        userRepo.save(user);
        return currentTweet.getLikes();
    }
}
