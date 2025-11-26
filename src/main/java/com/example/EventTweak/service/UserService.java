package com.example.EventTweak.service;

import com.example.EventTweak.model.Address;
import com.example.EventTweak.model.User;
import com.example.EventTweak.model.dto.ImageData;
import com.example.EventTweak.model.io.user.ApiResponse;
import com.example.EventTweak.model.io.user.UserProfileRequest;
import com.example.EventTweak.model.io.user.UserProfileResponse;
import com.example.EventTweak.security.config.jwt.JwtService;
import com.example.EventTweak.repository.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Service
public class UserService {

    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private CloudinaryService cloudinaryService;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, CloudinaryService cloudinaryService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cloudinaryService = cloudinaryService;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    public static boolean isStrongPassword(String password){
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    public ResponseEntity<Object> registerUser(String email, String password) {
        if(email == null || email == "" || !isValidEmail(email)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Please enter valid email");
        }
        if(!isStrongPassword(password)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Please enter strong password");
        }

        email = email.toLowerCase();

        if (userRepo.findByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exist");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepo.save(user);

        if(savedUser == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Saving User");
        }

//          return new ResponseEntity<>(new ApiResponse<>(201,"User Created Successfully",null),HttpStatus.CREATED);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201,"User registered successfully",null));
    }




    public ResponseEntity<Object> loginUser(String email, String password) {

        if(email == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide email");
        }
        if(password == null || password == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide password");
        }

        email = email.toLowerCase();

        User savedUser = userRepo.findByEmail(email);
        if(savedUser == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }

        Authentication authentication = null;
        try{
             authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        }

        Map<String,Object> responseData = new HashMap<>();
        responseData.put("role","user");
        responseData.put("token", jwtService.generateToken(email, "user"));
        responseData.put("name",savedUser.getFullName());
        responseData.put("id", savedUser.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(),"Login Successful",responseData));

    }


    public ResponseEntity<Object> updateUserProfile(MultipartFile file , UserProfileRequest userProfile)  {

        if((file != null && !userProfile.getProfileImageData().getPublicId().isEmpty()) ||
                (!userProfile.getProfileImageData().getPublicId().isEmpty() && userProfile.getProfileImageData().getUrl().isEmpty())){
            // delete the image from cloud
            try{
                Map deleteFileResult = cloudinaryService.deleteSingleFile(userProfile.getProfileImageData().getPublicId());
                if(deleteFileResult.get("result").equals("ok")){
                    userProfile.setProfileImageData(new ImageData());
                }

            }catch (IOException e){
                throw new RuntimeException(e);
            }

        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        User presentUser = userRepo.findByEmail(authentication.getName());
        if(presentUser == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        }

        presentUser.setGender(userProfile.getGender());
        presentUser.setAddress(userProfile.getAddress());
        presentUser.setFullName(userProfile.getFullName());
        presentUser.setPhoneNo(userProfile.getPhoneNo());

        if( file!= null ){
            Map uploadedFileResult = null;
        try{
            uploadedFileResult = cloudinaryService.uploadSingleFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageData profileImageData = new ImageData();
        profileImageData.setUrl(uploadedFileResult.get("secure_url").toString());
        profileImageData.setPublicId(uploadedFileResult.get("public_id").toString());
        presentUser.setProfileImageData(profileImageData);
        }else{
            presentUser.setProfileImageData(ImageData.builder()
                            .url(userProfile.getProfileImageData().getUrl())
                            .publicId(userProfile.getProfileImageData().getPublicId())
                            .build());
        }

        User savedUser = userRepo.save(presentUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse(HttpStatus.OK.value(),
                        "Profile Updated Successfully",
                        UserProfileResponse.builder()
                                .fullName(savedUser.getFullName())
                                .profileImageData(savedUser.getProfileImageData())
                                .address(savedUser.getAddress())
                                .gender(savedUser.getGender())
                                .phoneNo(savedUser.getPhoneNo())
                                .build()
                ));

    }

    public ResponseEntity<Object> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User presentUser = userRepo.findByEmail(email);

        if(presentUser == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(),"successful",UserProfileResponse
                        .builder()
                        .fullName(presentUser.getFullName())
                        .address(presentUser.getAddress())
                        .profileImageData(presentUser.getProfileImageData())
                        .gender(presentUser.getGender())
                        .phoneNo(presentUser.getPhoneNo())
                        .email(presentUser.getEmail())
                        .id(presentUser.getId())
                        .build()
                ));
    }

    public ResponseEntity<Object> getAddress() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if(email == null || email.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unauthorized");
        }

        User user = userRepo.findByEmail(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "successful",
                        Address.builder()
                                .area(user.getAddress().getArea())
                                .city(user.getAddress().getCity())
                                .state(user.getAddress().getState())
                                .houseNo(user.getAddress().getHouseNo())
                                .locality(user.getAddress().getLocality())
                                .pinCode(user.getAddress().getPinCode())
                                .build()
                        ));
    }

    public ResponseEntity<Object> deleteUserAccount(String userId) {
        userRepo.deleteById(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .message("successful")
                        .status(HttpStatus.OK.value())
                        .build());
    }
}
