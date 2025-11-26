package com.example.EventTweak.model.io.user;

import com.example.EventTweak.model.Address;
import com.example.EventTweak.model.dto.ImageData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorProfileRequest {
    private String fullName ="";
    private String phoneNo =""; // otp verification
    private Address address ;
    private String gender ="";
    private ImageData profileImageData;
    private String businessType= ""; // catering, decorator, etc
    private List<String> services; // eg. catering - veg/non-veg meals, etc
    private List<ImageData> sampleWorkData; // previous work image urls
}
