package com.example.EventTweak.controller;

import com.example.EventTweak.model.EventBooking;
import com.example.EventTweak.model.EventBookingRequest;
import com.example.EventTweak.model.VendorBookingRequestObject;
import com.example.EventTweak.model.io.user.AddVendorToBookingRequestObj;
import com.example.EventTweak.model.io.user.SaveEventRequestObj;
import com.example.EventTweak.model.io.user.UpdateVenueForBookingRequestObj;
import com.example.EventTweak.service.EventBookingService;
import com.example.EventTweak.service.Services;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventBookingController {

    private EventBookingService eventBookingService;


    public EventBookingController(EventBookingService eventBookingService){
        this.eventBookingService = eventBookingService;
    }

    @PostMapping("/saveEvent")
    public ResponseEntity<Object> saveEvent(@RequestBody SaveEventRequestObj saveEventRequestObj){
        return eventBookingService.saveEvent(saveEventRequestObj);
    }

    @GetMapping("/getEventsForVenue") ResponseEntity<Object> getEventsForVenue(@RequestParam String venueId){
       return  eventBookingService.getEventsForVenue(venueId);
    }

    @GetMapping("/getEventsForVendor") ResponseEntity<Object> getEventsForVendor(@RequestParam String vendorId){
        return  eventBookingService.getEventsForVendor(vendorId);
    }



    @PostMapping("/booking-request")
    public ResponseEntity<Object> saveBookingRequest(@RequestBody EventBookingRequest eventBookingRequest){
        return eventBookingService.saveEventBookingRequest(eventBookingRequest);
    }

    @PostMapping("/addVendorsToBookingRequest")
    public ResponseEntity<Object> addVendorsToBookingRequest(@RequestBody AddVendorToBookingRequestObj obj){
        return eventBookingService.addVendorsToBookingRequest(obj.getVendorsList(), obj.getRequestId());
    }

    @PostMapping("/updateVenueForBookingRequest")
    public ResponseEntity<Object> updateVenueForBookingRequest(@RequestBody UpdateVenueForBookingRequestObj obj){

        return eventBookingService.updateVenueForBookingRequest(obj);
    }

    @GetMapping("/getAllEventBookingRequestsForClient")
    public ResponseEntity<Object> getAllEventBookingRequestsForClient(@RequestParam String clientId){
        return eventBookingService.getAllEventBookingRequestsForClient(clientId);
    }

    @GetMapping("/getAllEventBookingsForClient")
    public ResponseEntity<Object> getAllEventBookingsForClient(@RequestParam String clientId){
        return eventBookingService.getAllEventBookingsForClient(clientId);
    }

    @GetMapping("/rejectVendorByClient")
    public ResponseEntity<Object> rejectVendorByClient(@RequestParam String vendorId, @RequestParam String requestId){
        return eventBookingService.rejectVendorByClient(vendorId, requestId);
    }

    @GetMapping("/cancelEventBooking")
    public ResponseEntity<Object> cancelEventBooking(@RequestParam String eventBookingId){
        return eventBookingService.cancelEventBooking(eventBookingId);
    }


}
