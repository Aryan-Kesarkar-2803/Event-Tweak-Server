package com.example.EventTweak.model.io.user;

import com.example.EventTweak.model.VendorBookingRequestObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateServiceRequestClientObj {
    private String serviceBookingRequestId;
    private VendorBookingRequestObject vendorObj;

}
