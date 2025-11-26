package com.example.EventTweak.model.io.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestStatusObjForVendor {
    private String requestId;
    private String vendorId;
    private String amount;
    private boolean userAccepted;
    private boolean vendorAccepted;
    private boolean vendorRejected;

}

