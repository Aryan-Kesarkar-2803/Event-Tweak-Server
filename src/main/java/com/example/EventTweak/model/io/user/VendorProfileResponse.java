package com.example.EventTweak.model.io.user;

import com.example.EventTweak.model.Address;
import com.example.EventTweak.model.dto.ImageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorProfileResponse {
    private String Id;
    private String fullName;
    private String phoneNo; // otp verification
    private Address address;
    private String gender;
    private ImageData profileImageData;
    private String businessType;
    private List<String> services;
    private List<ImageData> sampleWorkData;
    private List<String> completedBookings;
    private List<String> currentBookings;

}
