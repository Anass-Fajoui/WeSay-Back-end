package org.example.twitterproject.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String passowrd;
}
