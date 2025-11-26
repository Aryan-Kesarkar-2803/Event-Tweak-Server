package com.example.EventTweak.model.io.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BookingRequestStatusObjForVenue {
    private String id;
    private boolean venueAccepted;
    private boolean venueRejected;
}

