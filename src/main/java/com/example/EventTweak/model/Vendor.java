package com.example.EventTweak.model;

import com.example.EventTweak.model.dto.ImageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "vendors")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vendor {

    @Id
    private String id;
    private String email; // email verification
    private String password;

    private String fullName;
    private String phoneNo; // otp verification
    private Address address;
    private String gender;
    private ImageData profileImageData;
    private List<String> completedBookings;
    private List<String> currentBookings;
    private String businessType; // catering, decorator, etc
    private List<String> services; // eg. catering - veg/non-veg meals, etc
    private List<ImageData> sampleWork; // previous work image urls

}


