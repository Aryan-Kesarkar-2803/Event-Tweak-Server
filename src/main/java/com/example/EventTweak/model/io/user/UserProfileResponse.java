package com.example.EventTweak.model.io.user;

import com.example.EventTweak.model.Address;
import com.example.EventTweak.model.dto.ImageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String fullName;
    private String phoneNo; // otp verification
    private Address address;
    private String gender;
    private ImageData profileImageData;
    private String email;
    private String id;
}
