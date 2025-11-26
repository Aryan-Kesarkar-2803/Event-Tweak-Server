package com.example.EventTweak.model.io.user;

import com.example.EventTweak.model.EventBooking;
import com.example.EventTweak.model.EventBookingRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveEventRequestObj {
    private String bookingRequestId = "";
    private EventBooking eventBooking;
}
