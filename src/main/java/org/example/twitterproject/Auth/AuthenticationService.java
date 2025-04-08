package org.example.twitterproject.Auth;

import lombok.RequiredArgsConstructor;
import org.example.twitterproject.Exceptions.EmailOrUsernameInUseException;
import org.example.twitterproject.config.JwtService;
import org.example.twitterproject.models.Role;
import org.example.twitterproject.models.User;
import org.example.twitterproject.repositories.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo repo;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authManager;


    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        var error = new EmailOrUsernameInUseException();

        if (repo.existsByEmail(request.getEmail())) {
            error.setEmailError(true);
        }
        if (repo.existsByUserName(request.getUserName())){
            error.setUsernameError(true);
        }
        if (error.isReadyToThrow()){
            throw error;
        }

        repo.save(user);

        var dUser = repo.findByEmail(request.getEmail()).get();
        var jwtToken = jwtService.generateToken(dUser, dUser.getId());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repo.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user, user.getId());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
