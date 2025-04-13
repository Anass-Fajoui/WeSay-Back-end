package org.example.twitterproject.controllers;

import org.example.twitterproject.models.DisplayUserDTO;
import org.example.twitterproject.models.User;
import org.example.twitterproject.models.UserDTO;
import org.example.twitterproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<DisplayUserDTO> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/user/{id}")
    public DisplayUserDTO getUser(@PathVariable int id){
        return userService.getUser(id);
    }

    @GetMapping("/myuser")
    public User getMyUser(@RequestHeader("Authorization") String Token){
        String token = Token.substring(7);
        return userService.getMyUser(token);
    }

    @GetMapping("/tweetsliked")
    public List<Integer> getMyTweetsLiked(@RequestHeader("Authorization") String Token){
        String token = Token.substring(7);
        return userService.getMyTweetsLiked(token);
    }

    @PutMapping("/myuser")
    public ResponseEntity editMyUser(@RequestBody UserDTO user,
                                   @RequestHeader("Authorization") String Token){
        String token = Token.substring(7);
        userService.updateMyUser(user, token);
        return ResponseEntity.status(HttpStatus.OK).body("User Edited Successfully");
    }

    @DeleteMapping("/myuser")
    public ResponseEntity deleteMyUser(@RequestHeader("Authorization") String Token){
        String token = Token.substring(7);
        userService.deleteMyUser(token);
        return ResponseEntity.status(HttpStatus.OK).body("User Deleted Successfully");
    }
}
