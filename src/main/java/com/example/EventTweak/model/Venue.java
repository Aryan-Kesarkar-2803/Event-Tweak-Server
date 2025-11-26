package com.example.EventTweak.model;

import com.example.EventTweak.model.dto.ImageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "venues")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Venue {
    @Id
    private String id;
    private String email; // email verification
    private String password;

    private String coordinatorName;
    private String phoneNo; // otp verification
    private Address address;
    private ImageData profileImageData;
    private List<String> completedBookings;
    private List<String> currentBookings;
    private List<String> services; // eg. catering - veg/non-veg meals, etc
    private List<ImageData> sampleWork; // previous work image urls or venue images if vendor is venue
    private String guestCapacity; // only if vendor type is venue
    private List<String> amenities; // only if vendor type is venue
    private String venueName; // only if vendor is venue
    private String chargesPerDay; // only if vendor is venue
}
