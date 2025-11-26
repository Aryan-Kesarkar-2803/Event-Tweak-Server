package com.example.EventTweak.service;

import com.example.EventTweak.model.EventBooking;
import com.example.EventTweak.model.EventBookingRequest;
import com.example.EventTweak.model.Vendor;
import com.example.EventTweak.model.Venue;
import com.example.EventTweak.model.dto.ImageData;
import com.example.EventTweak.model.io.user.*;
import com.example.EventTweak.repository.*;
import com.example.EventTweak.security.config.jwt.JwtService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class VenueService {

    private VendorRepo vendorRepo;
    private VendorRepoImpl vendorRepoImpl;
    private final EventBookingRepo eventBookingRepo;
    private final VenueRepo venueRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CloudinaryService cloudinaryService;
    private final VenueRepoImpl venueRepoImpl;
    private final EventBookingRequestRepo eventBookingRequestRepo;
    private final EventBookingRequestRepoImpl eventBookingRequestRepoImpl;

    public VenueService(VendorRepo vendorRepo, VendorRepoImpl vendorRepoImpl, BCryptPasswordEncoder passwordEncoder, VenueRepo venueRepo, AuthenticationManager authenticationManager, JwtService jwtService, CloudinaryService cloudinaryService, EventBookingRepo eventBookingRepo, VenueRepoImpl venueRepoImpl, EventBookingRequestRepo eventBookingRequestRepo, EventBookingRequestRepoImpl eventBookingRequestRepoImpl){
        this.vendorRepo = vendorRepo;
        this.vendorRepoImpl = vendorRepoImpl;
        this.passwordEncoder = passwordEncoder;
        this.venueRepo = venueRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cloudinaryService = cloudinaryService;
        this.eventBookingRepo = eventBookingRepo;
        this.venueRepoImpl = venueRepoImpl;
        this.eventBookingRequestRepo = eventBookingRequestRepo;
        this.eventBookingRequestRepoImpl = eventBookingRequestRepoImpl;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    public static boolean isStrongPassword(String password){
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    public ResponseEntity<Object> getVenues(String state, String city, String date){

        // getting venues on state and city
        List<VenueProfileResponse> venueForCity= venueRepoImpl.getVenuesByStateAndCity(state,city);

        List<EventBooking> eventBookingsForDate = eventBookingRepo.findByDate(date);

        List<VenueProfileResponse> res = new ArrayList<>();

        for(VenueProfileResponse temp: venueForCity){
            if(!eventBookingsForDate.stream().anyMatch(booking -> booking.getVenue().equals(temp.getId()))){
                res.add(temp);
            }
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("successful")
                                .data(res)
                                .build()
                );
    }

    public ResponseEntity<Object> registerVenue(String email, String password) {
        if(email == null || email == "" || !isValidEmail(email)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Please enter valid email");
        }
        if(!isStrongPassword(password)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Please enter strong password");
        }

        email = email.toLowerCase();

        if (venueRepo.findByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Venue Already Exist");
        }

        Venue venue = new Venue();
        venue.setEmail(email);
        venue.setPassword(passwordEncoder.encode(password));

        Venue savedVenue = venueRepo.save(venue);

        if(savedVenue == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Saving Vendor");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(),"Venue Registered Successfully",null));
    }

    public ResponseEntity<Object> loginVenue(String email, String password) {

        if(email == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide email");
        }
        if(password == null || password == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide password");
        }

        email = email.toLowerCase();

        Venue savedVenue = venueRepo.findByEmail(email);
        if(savedVenue == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vendor not found");
        }

        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException e) {
            System.out.println("Exception - "+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token",jwtService.generateToken(email,"venue"));
        responseData.put("role","venue");
        responseData.put("name",savedVenue.getCoordinatorName());
        responseData.put("id",savedVenue.getId());
        return ResponseEntity
                .status(200)
                .body(new ApiResponse<>(200, "Login successful",responseData));
    }

    public ResponseEntity<Object> updateVenueProfile(MultipartFile photoFile, MultipartFile[] sampleWorkFiles, VenueProfileRequest venueProfile)  {
        if((photoFile != null && !venueProfile.getProfileImageData().getPublicId().isEmpty()) ||
                (!venueProfile.getProfileImageData().getPublicId().isEmpty() && venueProfile.getProfileImageData().getUrl().isEmpty())){
            // delete the image from cloud
            try{
                Map deleteFileResult = cloudinaryService.deleteSingleFile(venueProfile.getProfileImageData().getPublicId());
                if(deleteFileResult.get("result").equals("ok")){
                    venueProfile.setProfileImageData(new ImageData());
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            Venue venue = venueRepo.findByEmail((authentication.getName()));
            venue.setAddress(venueProfile.getAddress());
            venue.setCoordinatorName(venueProfile.getCoordinatorName());
            venue.setPhoneNo(venueProfile.getPhoneNo());
            venue.setServices(venueProfile.getServices());
            venue.setSampleWork(venueProfile.getSampleWorkData());
            venue.setAmenities(venueProfile.getAmenities());
            venue.setChargesPerDay(venueProfile.getChargesPerDay());
            venue.setVenueName(venueProfile.getVenueName());
            venue.setGuestCapacity(venueProfile.getGuestCapacity());

            if( photoFile!= null ){
                Map uploadedFileResult = null;
                try{
                    uploadedFileResult = cloudinaryService.uploadSingleFile(photoFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ImageData profileImageData = new ImageData();
                profileImageData.setUrl(uploadedFileResult.get("secure_url").toString());
                profileImageData.setPublicId(uploadedFileResult.get("public_id").toString());
                venue.setProfileImageData(profileImageData);
            }else{
                venue.setProfileImageData(ImageData.builder()
                        .url(venueProfile.getProfileImageData().getUrl())
                        .publicId(venueProfile.getProfileImageData().getPublicId())
                        .build());
            }

            if( sampleWorkFiles != null && sampleWorkFiles.length > 0 ){
                List<Map> result = null;
                try{
                    result =  cloudinaryService.uploadMultipleFile(sampleWorkFiles);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                List<ImageData> sampleWork = new ArrayList<>(venueProfile.getSampleWorkData());

                for(Map res: result){
                    ImageData imageData = new ImageData();
                    imageData.setUrl(res.get("secure_url").toString());
                    imageData.setPublicId(res.get("public_id").toString());
                    sampleWork.add(imageData);
                }
                venue.setSampleWork(sampleWork);
            }

            Venue savedVenue = venueRepo.save(venue);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse(HttpStatus.OK.value(),
                            "Profile Updated Successfully",
                            VenueProfileResponse.builder()
                                    .coordinatorName(savedVenue.getCoordinatorName())
                                    .profileImageData(savedVenue.getProfileImageData())
                                    .address(savedVenue.getAddress())
                                    .phoneNo(savedVenue.getPhoneNo())
                                    .services(savedVenue.getServices())
                                    .sampleWorkData(savedVenue.getSampleWork())
                                    .guestCapacity(savedVenue.getGuestCapacity())
                                    .amenities(savedVenue.getAmenities())
                                    .venueName(savedVenue.getVenueName())
                                    .chargesPerDay(savedVenue.getChargesPerDay())
                                    .build()
                    ));





    }

    public ResponseEntity<Object> getProfileData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

           Venue venue = venueRepo.findByEmail(email);
            if(venue == null){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse(HttpStatus.OK.value(),
                            "Profile Updated Successfully",
                            VenueProfileResponse.builder()
                                    .coordinatorName(venue.getCoordinatorName())
                                    .profileImageData(venue.getProfileImageData())
                                    .address(venue.getAddress())
                                    .phoneNo(venue.getPhoneNo())
                                    .services(venue.getServices())
                                    .sampleWorkData(venue.getSampleWork())
                                    .guestCapacity(venue.getGuestCapacity())
                                    .amenities(venue.getAmenities())
                                    .venueName(venue.getVenueName())
                                    .chargesPerDay(venue.getChargesPerDay())
                                    .build()
                    ));
        }

    public ResponseEntity<Object> getVenueById(String venueId) {
        Optional<Venue> venue = venueRepo.findById(venueId);
        VenueProfileResponse venueProfileResponse = new VenueProfileResponse();
        venueProfileResponse.setServices(venue.get().getServices());
        venueProfileResponse.setAmenities(venue.get().getAmenities());
        venueProfileResponse.setVenueName(venue.get().getVenueName());
        venueProfileResponse.setAddress(venue.get().getAddress());
        venueProfileResponse.setEmail(venue.get().getEmail());
        venueProfileResponse.setChargesPerDay(venue.get().getChargesPerDay());
        venueProfileResponse.setCoordinatorName(venue.get().getCoordinatorName());
        venueProfileResponse.setSampleWorkData(venue.get().getSampleWork());
        venueProfileResponse.setProfileImageData(venue.get().getProfileImageData());


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(HttpStatus.OK.value())
                                .data(venueProfileResponse)
                                .message("successful")
                                .build()
                );
    }

    public ResponseEntity<Object> getBookingRequestsForVenue(String venueId) {
        List <EventBookingRequest> res = eventBookingRequestRepoImpl.getBookingRequestsForVenue(venueId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .data(res)
                        .build());
    }

    public ResponseEntity<Object> updateBookingRequestStatusForVenue(BookingRequestStatusObjForVenue obj) {
        UpdateResult res = eventBookingRequestRepoImpl.updateBookingRequestStatusForVenue(obj);
        return ResponseEntity
                .status(HttpStatus.OK)
                        .body(ApiResponse
                                .builder()
                                .status(HttpStatus.OK.value())
                                .message("successful")
                                .data(res)
                                .build());
    }
}
