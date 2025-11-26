package com.example.EventTweak.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "eventBookingRequests")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventBookingRequest {
    @Id
    private String id;
    private String clientName;
    private String clientEmail;
    private String clientId;
    private String clientPhoneNo;
    private String eventName;
    private boolean isActive = true;
    private String date;
    private String time;
    private String eventType;
    private String noOfGuests;
    private Address venueAddress;
    private VenueBookingRequestObject venue; // id
    private List<VendorBookingRequestObject> selectedServices; // ids
}
