package com.example.EventTweak.model.io.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestStatusObjForVendor {
    private String vendorId;
    private String serviceRequestId;
    private String amount;
    private boolean vendorAccepted;
    private boolean vendorRejected;
}
