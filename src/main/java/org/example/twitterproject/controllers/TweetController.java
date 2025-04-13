package org.example.twitterproject.controllers;

import org.example.twitterproject.models.Tweet;
import org.example.twitterproject.models.TweetDTO;
import org.example.twitterproject.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @GetMapping("/tweets")
    public List<Tweet> getTweets(){
        return tweetService.getTweets();
    }

    @GetMapping("/tweet/{id}")
    public ResponseEntity getTweet(@PathVariable int id){

        Tweet tweet = tweetService.getTweet(id);
        return ResponseEntity.status(HttpStatus.OK).body(tweet);

    }
    @GetMapping("user/{id}/tweets")
    public List<Tweet> getTweetsByUser(@PathVariable int id){
        List<Tweet> tweets = tweetService.getTweetsByUser(id);
        return tweets;
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
        tweetService.like(id, token);
        return ResponseEntity.status(HttpStatus.OK).body("Tweet Liked successfully");

    }
    @PatchMapping("/tweet/{id}/unlike")
    public ResponseEntity unlikeTweet(@PathVariable int id,
                                      @RequestHeader("Authorization") String Token){

        String token = Token.substring(7);
        tweetService.unlike(id, token);
        return ResponseEntity.status(HttpStatus.OK).body("Tweet Liked successfully");

    }
}
