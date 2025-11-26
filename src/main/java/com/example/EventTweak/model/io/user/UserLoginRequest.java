package com.example.EventTweak.model.io.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserLoginRequest {
    private String email;
    private String password;
}
