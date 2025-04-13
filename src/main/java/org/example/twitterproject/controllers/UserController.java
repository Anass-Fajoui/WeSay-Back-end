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

    @GetMapping("/userInfo/{id}")
    public User getUser(@PathVariable int id,
                        @RequestHeader("Authorization") String Token){
        return userService.getUserInfo(id, Token);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity editUser(@RequestBody UserDTO user, @PathVariable int id,
                                   @RequestHeader("Authorization") String Token){
        String token = Token.substring(7);
        userService.updateUser(user, id, Token);
        return ResponseEntity.status(HttpStatus.OK).body("User Edited Successfully");
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUser(@PathVariable int id,
                           @RequestHeader("Authorization") String Token){
        String token = Token.substring(7);
        userService.deleteUser(id, Token);
        return ResponseEntity.status(HttpStatus.OK).body("User Created Successfully");
    }
}
