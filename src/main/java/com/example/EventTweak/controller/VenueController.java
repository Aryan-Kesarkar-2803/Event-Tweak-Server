package com.example.EventTweak.controller;

import com.example.EventTweak.model.io.user.*;
import com.example.EventTweak.service.VenueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/venue")
public class VenueController {

    private VenueService venueService;

    public VenueController(VenueService venueService){
        this.venueService = venueService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerVendor(@RequestBody UserRegisterRequest user){
        return venueService.registerVenue(user.getEmail(), user.getPassword());
    }
    @PostMapping("/login")
    public ResponseEntity<Object> loginVendor(@RequestBody UserLoginRequest user){
        return venueService.loginVenue(user.getEmail(), user.getPassword());
    }

    @GetMapping("/get-venues")
    public ResponseEntity<Object> getVenues(@RequestParam(required = false,name = "city") String city,
                                            @RequestParam(required = false, name = "state") String state,
                                            @RequestParam(required = false, name = "date") String date){
        return venueService.getVenues(state, city,date);
    }
    @PostMapping("/update-profile")
    public ResponseEntity<Object> profileUpdation(
            @RequestPart(value = "photoFile", required = false) MultipartFile photoFile
            ,@RequestPart(value = "sampleWorkFiles", required = false) MultipartFile[] sampleWorkFiles
            ,@RequestPart("venueProfile") String venueProfileJson
    ) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        VenueProfileRequest venueProfile = mapper.readValue(venueProfileJson, VenueProfileRequest.class);
        return venueService.updateVenueProfile(photoFile, sampleWorkFiles, venueProfile);
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfileData(){
        return venueService.getProfileData();
    }

    @GetMapping("/getVenueById")
    public ResponseEntity<Object> getVenueById(@RequestParam String venueId){
        return venueService.getVenueById(venueId);
    }

    @GetMapping("/getBookingRequests")
    public ResponseEntity<Object> getBookingRequestsForVenue(@RequestParam String venueId){
        return venueService.getBookingRequestsForVenue(venueId);
    }

    @PostMapping("/updateBookingRequestStatus")
    public ResponseEntity<Object> updateBookingRequestStatusForVenue(@RequestBody BookingRequestStatusObjForVenue obj){
        return venueService.updateBookingRequestStatusForVenue(obj);
    }



}
