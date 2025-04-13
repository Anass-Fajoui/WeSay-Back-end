package org.example.twitterproject.services;

import org.example.twitterproject.Exceptions.EntityNotFoundException;
import org.example.twitterproject.config.JwtService;
import org.example.twitterproject.models.DisplayUserDTO;
import org.example.twitterproject.models.UserDTO;
import org.example.twitterproject.models.User;
import org.example.twitterproject.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

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

    public User getUserInfo(int id, String token){
        int tokenId = jwtService.extractId(token);

        if (!userRepo.existsById(tokenId)){
            throw new EntityNotFoundException("User "+tokenId+" in token not found");
        }

        User user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User "+ id + " Not Found"));

        if (tokenId != id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
        }

        return user;
    }

    public void updateUser(UserDTO updateUser, int id, String token){
        int tokenId = jwtService.extractId(token);

        if (!userRepo.existsById(tokenId)){
            throw new EntityNotFoundException("User "+tokenId+" in token not found");
        }

        User user = userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User "+ id + " Not Found"));

        if (tokenId != id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
        }
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setUserName(updateUser.getUserName());
        user.setEmail(updateUser.getEmail());
        user.setPassword(updateUser.getPassword());

        userRepo.save(user);
    }

    public void deleteUser(int id, String token){
        int tokenId = jwtService.extractId(token);
        if (!userRepo.existsById(tokenId)){
            throw new EntityNotFoundException("User "+tokenId+" in token not found");
        }

        if (!userRepo.existsById(id)){
            throw new EntityNotFoundException("User "+id+" not found");
        }

        if (tokenId != id){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this resource.");
        }

        userRepo.deleteById(id);
    }
}
