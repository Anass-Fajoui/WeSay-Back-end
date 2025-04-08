package org.example.twitterproject.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailOrUsernameInUseException.class)
    public ResponseEntity<Map<String, Boolean>> handleEmailOrUsernameInUseException(EmailOrUsernameInUseException ex){
        Map<String, Boolean> errorBody = new HashMap<>();
        errorBody.put("emailError", ex.isEmailError());
        errorBody.put("usernameError", ex.isUsernameError());
        System.out.println(errorBody);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialException(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Email or Password");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
