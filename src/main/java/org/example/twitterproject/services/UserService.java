package org.example.twitterproject.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.twitterproject.Exceptions.EmailOrUsernameInUseException;
import org.example.twitterproject.Exceptions.EntityNotFoundException;
import org.example.twitterproject.config.JwtService;
import org.example.twitterproject.models.DisplayUserDTO;
import org.example.twitterproject.models.Tweet;
import org.example.twitterproject.models.UserDTO;
import org.example.twitterproject.models.User;
import org.example.twitterproject.repositories.TweetRepo;
import org.example.twitterproject.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepo userRepo;


    private final JwtService jwtService;


    private final PasswordEncoder passwordEncoder;

    private final TweetRepo tweetRepo;

    public List<DisplayUserDTO> getUsers() {
        List<User> users = userRepo.findAll();
        List<DisplayUserDTO> userDTOs = users.stream()
                .map(DisplayUserDTO::new)
                .collect(Collectors.toList());
        return userDTOs;
    }

    public DisplayUserDTO getUser(int id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User "+ id + " Not Found"));
        DisplayUserDTO dUser = new DisplayUserDTO(user);
        return dUser;
    }

    public User getMyUser(String token){
        int tokenId = jwtService.extractId(token);
        User user = userRepo.findById(tokenId)
                .orElseThrow(() -> new EntityNotFoundException("User "+ tokenId + " Not Found"));

        return user;
    }

    public void updateMyUser(UserDTO updateUser, String token){
        int tokenId = jwtService.extractId(token);
        User user = userRepo.findById(tokenId)
                .orElseThrow(() -> new EntityNotFoundException("User "+ tokenId + " Not Found"));

        var error = new EmailOrUsernameInUseException();

        if (userRepo.existsByEmail(updateUser.getEmail()) &&
                !updateUser.getEmail().equals(user.getEmail())) {
            error.setEmailError(true);
        }
        if (userRepo.existsByUserName(updateUser.getUserName())  &&
                !updateUser.getUserName().equals(user.getUserName())){
            error.setUsernameError(true);
        }
        if (error.isReadyToThrow()){
            throw error;
        }

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setUserName(updateUser.getUserName());
        user.setEmail(updateUser.getEmail());
        user.setPassword(passwordEncoder.encode(updateUser.getPassword()));


        userRepo.save(user);
    }

    @Transactional
    public void deleteMyUser(String token){
        int tokenId = jwtService.extractId(token);
        User user = userRepo.findById(tokenId)
                .orElseThrow(() -> new EntityNotFoundException("User "+ tokenId + " Not Found"));

        for (Tweet likedTweet : user.getTweetsLiked()){
            likedTweet.unlikeDelete(user);
        }
        user.getTweetsLiked().clear();
        user.getTweets().clear();
        userRepo.delete(user);
    }

    public List<Integer> getMyTweetsLiked(String token) {
        int tokenId = jwtService.extractId(token);
        User user = userRepo.findById(tokenId)
                .orElseThrow(() -> new UsernameNotFoundException("User "+ tokenId + " Not Found"));

        List<Tweet> likedTweets = user.getTweetsLiked();
        List<Integer> likedTweetIds = likedTweets.stream()
                .map(Tweet::getId)
                .collect(Collectors.toList());
        return likedTweetIds;
    }

    public List<User> getUsersBySearch(String query) {
        return userRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUserNameContainingIgnoreCase(query, query, query);
    }
}
