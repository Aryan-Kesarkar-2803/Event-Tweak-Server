package com.example.EventTweak.controller;

import com.example.EventTweak.model.ServiceBooking;
import com.example.EventTweak.model.ServiceBookingRequest;
import com.example.EventTweak.model.VendorBookingRequestObject;
import com.example.EventTweak.model.io.user.UpdateServiceRequestClientObj;
import com.example.EventTweak.service.Services;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/service")
public class ServiceController {
    private final Services services;

    public ServiceController(Services services) {
        this.services = services;
    }

    @PostMapping("/requestService")
    public ResponseEntity<Object> requestVendorService(@RequestBody ServiceBookingRequest obj){
        return services.requestVendorService(obj);
    }

    @GetMapping("/getAllServiceRequestsForClient")
    public ResponseEntity<Object> getAllEventBookingRequestsForClient(@RequestParam String clientId){
        return services.getAllServicesRequestsForClient(clientId);
    }
    @GetMapping("/getAllServiceRequestsForVendor")
    public ResponseEntity<Object> getAllServiceRequestsForVendor(@RequestParam String vendorId){
        return services.getAllServiceRequestsForVendor(vendorId);
    }

    @PostMapping("/updateServiceRequestForClient")
    public ResponseEntity<Object> updateServiceRequestForClient(@RequestBody UpdateServiceRequestClientObj obj){
        return services.updateServiceRequestForClient(obj.getServiceBookingRequestId(), obj.getVendorObj());
    }


    @PostMapping("/saveServiceBooking")
    public ResponseEntity<Object> saveServiceBooking(@RequestBody ServiceBooking obj){
        return services.saveServiceBooking(obj);
    }

    @GetMapping("/deleteServiceRequest")
    public ResponseEntity<Object> deleteServiceRequest(@RequestParam String serviceRequestId ){
        return services.deleteServiceRequest(serviceRequestId);
    }

    @GetMapping("/getAllServiceBookingsForClient")
    public ResponseEntity<Object> getAllServiceBookingsForClient(@RequestParam String clientId){
        return services.getAllServiceBookingsForClient(clientId);
    }

    @GetMapping("/getServiceBookingsForVendor") ResponseEntity<Object> getServiceBookingsForVendor(@RequestParam String vendorId){
        return  services.getServiceBookingsForVendor(vendorId);
    }

    @GetMapping("/cancelServiceBooking") ResponseEntity<Object> cancelServiceBooking(@RequestParam String serviceBookingId){
        return  services.cancelServiceBooking(serviceBookingId);
    }
}
