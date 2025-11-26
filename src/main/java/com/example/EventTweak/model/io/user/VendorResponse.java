package com.example.EventTweak.model.io.user;

import com.example.EventTweak.model.Address;
import com.example.EventTweak.model.dto.ImageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorResponse {

    private String email; // email verification


    private String fullName;
    private String phoneNo; // otp verification
    private Address address;
    private String gender;
    private ImageData profileImageData;

    private String businessType; // catering, decorator, etc
    private String guestCapacity;
    private List<String> services; // eg. catering - veg/non-veg meals, etc
    private List<ImageData> sampleWork; // previous work image urls
}
