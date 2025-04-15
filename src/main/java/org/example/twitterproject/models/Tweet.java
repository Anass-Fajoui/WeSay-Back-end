package org.example.twitterproject.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tweet implements Comparable<Tweet>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private Integer likes = 0;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToMany
    @JoinTable(
            name = "tweets_likers",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likers = new ArrayList<>();


    public int compareTo(Tweet tweet){
        return tweet.createdAt.compareTo(this.createdAt);
    }

    public void like(User user){
        if (likers.contains(user)){
            return;
        }
        likers.add(user);
        if (user.getTweetsLiked().contains(this)){
            return;
        }
        user.getTweetsLiked().add(this);
        this.likes++;
    }

    public void unlike(User user){
        likers.remove(user);
        user.getTweetsLiked().remove(this);
        this.likes--;
    }
    public void unlikeDelete(User user){
        likers.remove(user);
        this.likes--;
    }
}
