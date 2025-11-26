package com.example.EventTweak.model;

import com.example.EventTweak.model.io.user.VendorObj;
import com.example.EventTweak.model.io.user.VendorObjForService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceBooking {
    @Id
    private String id;
    private String clientName;
    private String clientId;
    private String clientPhoneNo;
    private boolean isActive = true;
    private String date;
    private String time;
    private Address address;
    private VendorObjForService vendor;
}
