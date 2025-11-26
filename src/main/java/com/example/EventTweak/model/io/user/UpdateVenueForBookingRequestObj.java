package com.example.EventTweak.model.io.user;

import com.example.EventTweak.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateVenueForBookingRequestObj {
    private String bookingRequestId;
    private String venue;
    private Address venueAddress;
}
