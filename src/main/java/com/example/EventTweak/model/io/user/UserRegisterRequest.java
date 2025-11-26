package com.example.EventTweak.model.io.user;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String email;
    private String password;
}
