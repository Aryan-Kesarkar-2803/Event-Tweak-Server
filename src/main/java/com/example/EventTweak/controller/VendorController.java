package com.example.EventTweak.controller;

import com.example.EventTweak.model.io.user.*;
import com.example.EventTweak.service.VendorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vendor")
public class VendorController {

    private VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }


    @PostMapping("/register")
    public ResponseEntity<Object> registerVendor(@RequestBody UserRegisterRequest user){
        return vendorService.registerVendor(user.getEmail(), user.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginVendor(@RequestBody UserLoginRequest user){
        return vendorService.loginVendor(user.getEmail(), user.getPassword());
    }

    @PostMapping("/update-profile")
    public ResponseEntity<Object> profileUpdation(
             @RequestPart(value = "photoFile", required = false) MultipartFile photoFile
            ,@RequestPart(value = "sampleWorkFiles", required = false) MultipartFile[] sampleWorkFiles
            ,@RequestPart("vendorProfile") String vendorProfileJson
             ) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        VendorProfileRequest vendorProfile = mapper.readValue(vendorProfileJson, VendorProfileRequest.class);
        return vendorService.updateVendorProfile(photoFile, sampleWorkFiles, vendorProfile);
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfileData(){
        return vendorService.getProfileData();
    }

    @GetMapping("/get-vendors")
    public ResponseEntity<Object> getVendors(@RequestParam(required = false,name = "city") String city,
                                             @RequestParam(required = false,name = "state") String state,
                                             @RequestParam(required = false,name = "businessType") String businessType){
            return vendorService.getVendors(state,city,businessType);
    }


    @PostMapping("/getVendorsByIds")
    public ResponseEntity<Object> getVendors(@RequestBody Map<String, ArrayList<String>> body){
        ArrayList<String> vendorIds = body.get("vendorIds");
        return vendorService.getVendorsByIds(vendorIds);
    }

    @PostMapping("/updateBookingRequestStatus")
    public ResponseEntity<Object> updateBookingRequestStatusForVendor(@RequestBody BookingRequestStatusObjForVendor obj){
        return vendorService.updateBookingRequestStatusForVendor(obj);
    }

    @PostMapping("/updateServiceRequestStatus")
    public ResponseEntity<Object> updateServiceRequestStatusForVendor(@RequestBody ServiceRequestStatusObjForVendor obj){
        return vendorService.updateServiceRequestStatusForVendor(obj);
    }

    @GetMapping("/getBookingRequests")
    public ResponseEntity<Object> getBookingRequestsForVendor(@RequestParam String vendorId){
        return vendorService.getBookingRequestsForVendor(vendorId);
    }

}
