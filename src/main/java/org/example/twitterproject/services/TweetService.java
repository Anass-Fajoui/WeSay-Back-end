package org.example.twitterproject.services;

import org.example.twitterproject.Exceptions.EntityNotFoundException;
import org.example.twitterproject.models.Tweet;
import org.example.twitterproject.models.TweetDTO;
import org.example.twitterproject.repositories.TweetRepo;

import java.util.List;

public class TweetService {

    private TweetRepo tweetRepo;

    public List<Tweet> getTweets(){
        List<Tweet> tweets = tweetRepo.findAll();
        return tweets;
    }

    public Tweet getTweet(int id) {
        Tweet tweet = tweetRepo.findById(id).orElse(null);
        if (tweet == null){
            throw new EntityNotFoundException("Tweet Not Found");
        }
        return tweet;
    }

    public void addTweet(TweetDTO tweetD) {
        Tweet tweet = Tweet.builder()
                .title(tweetD.getTitle())
                .content(tweetD.getContent())
                .likes(0)
                .build();

        tweetRepo.save(tweet);
    }

    public void updateTweet(TweetDTO tweetd, int id) {
        Tweet currentTweet = tweetRepo.findById(id).orElse(null);
        if (currentTweet == null){
            throw new EntityNotFoundException("Tweet Not Found");
        }
        currentTweet.setTitle(tweetd.getTitle());
        currentTweet.setContent(tweetd.getContent());
        tweetRepo.save(currentTweet);
    }

    public void deleteTweet(int id) {
        Tweet currentTweet = tweetRepo.findById(id).orElse(null);
        if (currentTweet == null){
            throw new EntityNotFoundException("Tweet Not Found");
        }
        tweetRepo.deleteById(id);
    }
}
