package com.example.EventTweak.model;

import com.example.EventTweak.model.io.user.VendorObj;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "eventBookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventBooking {
    @Id
    private String id;
    private String clientName;
    private String clientId;
    private String clientPhoneNo;
    private String eventName;
    private boolean isActive = true;
    private String date;
    private String time;
    private String eventType;
    private String noOfGuests;
    private Address address;
    private String venue; // id
    private List<VendorObj> services; // ids
}
