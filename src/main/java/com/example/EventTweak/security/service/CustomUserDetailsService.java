package com.example.EventTweak.security.service;

import com.example.EventTweak.model.User;
import com.example.EventTweak.model.Vendor;
import com.example.EventTweak.model.Venue;
import com.example.EventTweak.repository.VenueRepo;
import com.example.EventTweak.security.CustomUserDetails;
import com.example.EventTweak.security.CustomVendorDetails;
import com.example.EventTweak.repository.UserRepo;
import com.example.EventTweak.repository.VendorRepo;
import com.example.EventTweak.security.CustomVenueDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;
    private VendorRepo vendorRepo;
    private VenueRepo venueRepo;

    CustomUserDetailsService (UserRepo userRepo, VendorRepo vendorRepo, VenueRepo venueRepo){
        this.userRepo = userRepo;
        this.vendorRepo = vendorRepo;
        this.venueRepo = venueRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        try{
            User user = userRepo.findByEmail(email);

            if(user == null){
                // check in vendor repo;
                Vendor vendor = vendorRepo.findByEmail(email);
                if(vendor == null){
                    Venue venue = venueRepo.findByEmail(email);
                    if(venue == null){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User/Vendor not Found");
                    }
                    return new CustomVenueDetails(venue);
                }
                return new CustomVendorDetails(vendor);
            }

            return new CustomUserDetails(user);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
