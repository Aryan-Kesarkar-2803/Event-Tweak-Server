package com.example.EventTweak.model.io.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorObjForService {
    private String id;
    private String amount;
    private String instructions;
    private String businessType;
    private String vendorName;
}
