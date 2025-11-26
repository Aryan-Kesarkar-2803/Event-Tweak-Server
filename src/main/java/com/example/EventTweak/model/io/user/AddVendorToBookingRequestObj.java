package com.example.EventTweak.model.io.user;

import com.example.EventTweak.model.VendorBookingRequestObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddVendorToBookingRequestObj {
    List<VendorBookingRequestObject> vendorsList;
    String requestId;
}
