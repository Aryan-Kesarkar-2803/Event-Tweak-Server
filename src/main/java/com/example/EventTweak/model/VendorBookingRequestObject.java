package com.example.EventTweak.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorBookingRequestObject {
    private String amount;
    private String id;
    private String instructions;
    private String businessType;
    private String vendorName;
    private boolean userRejected;
    private boolean vendorAccepted;
    private boolean vendorRejected;
}
