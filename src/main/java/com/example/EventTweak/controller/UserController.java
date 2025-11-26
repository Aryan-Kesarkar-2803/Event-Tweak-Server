package com.example.EventTweak.controller;

import com.example.EventTweak.model.User;
import com.example.EventTweak.model.io.user.ApiResponse;
import com.example.EventTweak.model.io.user.UserLoginRequest;
import com.example.EventTweak.model.io.user.UserProfileRequest;
import com.example.EventTweak.model.io.user.UserRegisterRequest;
import com.example.EventTweak.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpRequest;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegisterRequest user){
        return userService.registerUser(user.getEmail(), user.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody UserLoginRequest user){
        return userService.loginUser(user.getEmail(), user.getPassword());
    }

    @PostMapping("/update-profile")
    public ResponseEntity<Object> profileUpdation(@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart("userProfile") String userProfileJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UserProfileRequest userProfile = mapper.readValue(userProfileJson, UserProfileRequest.class);
        return userService.updateUserProfile(file, userProfile);
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(){
        return userService.getProfile();
    }

    @GetMapping("/get-address")
    public ResponseEntity<Object> getUserAddress(){
        return userService.getAddress();
    }

    @GetMapping("/deleteUserAccount")
    public ResponseEntity<Object> deleteUserAccount(@RequestParam String userId){
        return userService.deleteUserAccount(userId);
    }





}
