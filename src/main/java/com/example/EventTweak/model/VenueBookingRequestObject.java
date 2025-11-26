package com.example.EventTweak.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VenueBookingRequestObject {
    private String id;
    private boolean venueAccepted;
    private boolean venueRejected;
}
