package org.example.twitterproject.controllers;

import org.example.twitterproject.models.Tweet;
import org.example.twitterproject.models.TweetDTO;
import org.example.twitterproject.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @GetMapping("/tweets")
    public Page<Tweet> getTweets(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size){

        return tweetService.getTweets(page, size);
    }

    @GetMapping("/tweet/{id}")
    public ResponseEntity getTweet(@PathVariable int id){

        Tweet tweet = tweetService.getTweet(id);
        return ResponseEntity.status(HttpStatus.OK).body(tweet);

    }

    @GetMapping("user/{id}/tweets")
    public Page<Tweet> getTweetsByUser(@PathVariable int id,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size){
        return tweetService.getTweetsByUser(id, page, size);
    }

    @PostMapping("/tweets/add")
    public ResponseEntity addTweet(@RequestBody TweetDTO tweetd,
                                   @RequestHeader("Authorization") String Token){

        String token = Token.substring(7);
        tweetService.addTweet(tweetd, token);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tweet Created");

    }

    @PutMapping("/tweet/{id}")
    public ResponseEntity updateTweet(@RequestBody TweetDTO tweetd,
                                      @PathVariable int id ,
                                      @RequestHeader("Authorization") String Token){

        String token = Token.substring(7);
        tweetService.updateTweet(tweetd, id, token);
        return ResponseEntity.status(HttpStatus.OK).body("Tweet Updated Successfully");

    }

    @DeleteMapping("/tweet/{id}")
    public ResponseEntity deleteTweet(@PathVariable int id,
                                      @RequestHeader("Authorization") String Token){

        String token = Token.substring(7);
        tweetService.deleteTweet(id, token);
        return ResponseEntity.status(HttpStatus.OK).body("Tweet Deleted Successfully");

    }

    @PatchMapping("/tweet/{id}/like")
    public ResponseEntity likeTweet(@PathVariable int id,
                                    @RequestHeader("Authorization") String Token){

        String token = Token.substring(7);
        int likes = tweetService.like(id, token);
        return ResponseEntity.status(HttpStatus.OK).body(likes);

    }
    @PatchMapping("/tweet/{id}/unlike")
    public ResponseEntity unlikeTweet(@PathVariable int id,
                                      @RequestHeader("Authorization") String Token){

        String token = Token.substring(7);
        int likes = tweetService.unlike(id, token);
        return ResponseEntity.status(HttpStatus.OK).body(likes);

    }
}