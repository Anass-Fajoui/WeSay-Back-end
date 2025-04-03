package org.example.twitterproject.controllers;

import org.example.twitterproject.models.Tweet;
import org.example.twitterproject.models.TweetDTO;
import org.example.twitterproject.repositories.TweetRepo;
import org.example.twitterproject.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class Controller {

    @Autowired
    private TweetService tweetService;

    @GetMapping("/tweets")
    public List<Tweet> getTweets(){
        return tweetService.getTweets();
    }

    @GetMapping("/tweet/{id}")
    public ResponseEntity getTweet(@PathVariable int id){
        try{
            Tweet tweet = tweetService.getTweet(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(tweet);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource Not Found");
        }
    }

    @PostMapping("/tweets/add")
    public ResponseEntity addTweet(@RequestBody TweetDTO tweetd){
        try{
            tweetService.addTweet(tweetd);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tweet Created");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/tweet/{id}")
    public ResponseEntity updateTweet(@RequestBody TweetDTO tweetd, @PathVariable int id){
        try{
            tweetService.updateTweet(tweetd, id);
            return ResponseEntity.status(HttpStatus.OK).body("Tweet Updated Successfully");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/tweet/{id}")
    public ResponseEntity deleteTweet(@PathVariable int id){
        try{
            tweetService.deleteTweet(id);
            return ResponseEntity.status(HttpStatus.OK).body("Tweet Deleted Successfully");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/tweet/{id}/like")
    public ResponseEntity likeTweet(@PathVariable int id){
        try {
            tweetService.like(id);
            return ResponseEntity.status(HttpStatus.OK).body("Tweet Liked successfully");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PatchMapping("/tweet/{id}/unlike")
    public ResponseEntity unlikeTweet(@PathVariable int id){
        try {
            tweetService.unlike(id);
            return ResponseEntity.status(HttpStatus.OK).body("Tweet Liked successfully");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
