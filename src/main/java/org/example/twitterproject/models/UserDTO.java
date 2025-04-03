package org.example.twitterproject.models;

import jakarta.persistence.Column;
import lombok.*;

//@Data
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String userName;

    @Column(unique = true)
    private String email;
}
