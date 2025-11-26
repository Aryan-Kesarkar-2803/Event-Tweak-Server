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
public class VenueProfileRequest {
    private String coordinatorName ="";
    private String phoneNo =""; // otp verification
    private Address address ;
    private ImageData profileImageData;
    private List<String> services; // eg. catering - veg/non-veg meals, etc
    private List<ImageData> sampleWorkData; // previous work image urls
    private String guestCapacity; // only if vendor type is venue
    private List<String> amenities; // only if vendor type is venue
    private String venueName; // only if vendor is venue
    private String chargesPerDay; // only if vendor is venue
}
