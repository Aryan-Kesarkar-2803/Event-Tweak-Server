package com.example.EventTweak.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceBookingRequest {
    @Id
    private String id;
    private String clientName;
    private String clientId;
    private String clientPhoneNo;
    private boolean isActive = true;
    private String date;
    private String time;
    private Address address;
    private VendorBookingRequestObject vendor;
}
