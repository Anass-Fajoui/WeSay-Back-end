package org.example.twitterproject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Role role;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Tweet> tweets = new ArrayList<>();

    public void addTweet(Tweet tweet){
        tweets.add(tweet);
    }
    public void updateTweet(Tweet updatedTweet) {
        tweets.stream()
                .filter(t -> t.getId().equals(updatedTweet.getId()))
                .findFirst()
                .ifPresent(t -> {
                    t.setTitle(updatedTweet.getTitle());
                    t.setContent(updatedTweet.getContent());
                    t.setLastModifiedAt(LocalDateTime.now());
                });
    }
    public void removeTweet(Tweet tweet){
        tweets.remove(tweet);
    }

    @ManyToMany(mappedBy = "likers")
    @JsonIgnore
    private List<Tweet> tweetsLiked = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public String getPassword() {
        return password;
    }

    public String getUserName(){
        return userName;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
