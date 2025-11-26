package com.example.EventTweak.service;

import com.example.EventTweak.model.EventBookingRequest;
import com.example.EventTweak.model.User;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class VendorService {

    private final VendorRepo vendorRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CloudinaryService cloudinaryService;
    private final VenueRepo venueRepo;
    private final VendorRepoImpl vendorRepoImpl;
    private final EventBookingRequestRepoImpl eventBookingRequestRepoImpl;
    private final ServiceBookingRequestRepoImpl serviceBookingRequestRepoImpl;

    public VendorService(VendorRepo vendorRepo, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, CloudinaryService cloudinaryService, VenueRepo venueRepo, VendorRepoImpl vendorRepoImpl, EventBookingRequestRepoImpl eventBookingRequestRepoImpl, ServiceBookingRequestRepoImpl serviceBookingRequestRepoImpl) {
        this.vendorRepo = vendorRepo;
        this.venueRepo = venueRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cloudinaryService = cloudinaryService;
        this.vendorRepoImpl = vendorRepoImpl;
        this.eventBookingRequestRepoImpl = eventBookingRequestRepoImpl;
        this.serviceBookingRequestRepoImpl = serviceBookingRequestRepoImpl;
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

    public ResponseEntity<Object> registerVendor(String email, String password) {
        if(email == null || email == "" || !isValidEmail(email)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Please enter valid email");
        }
        if(!isStrongPassword(password)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Please enter strong password");
        }

        email = email.toLowerCase();

        if (vendorRepo.findByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vendor Already Exist");
        }

        Vendor vendor = new Vendor();
        vendor.setEmail(email);
        vendor.setPassword(passwordEncoder.encode(password));

        Vendor savedVendor = vendorRepo.save(vendor);

        if(savedVendor == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Saving Vendor");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(),"Vendor Registered Successfully",null));
    }


    public ResponseEntity<Object> loginVendor(String email, String password) {

        if(email == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide email");
        }
        if(password == null || password == ""){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide password");
        }

        email = email.toLowerCase();

        Vendor savedVendor = vendorRepo.findByEmail(email);
        if(savedVendor == null){
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
        responseData.put("token",jwtService.generateToken(email,"vendor"));
        responseData.put("role","vendor");
        responseData.put("name",savedVendor.getFullName());
        responseData.put("id",savedVendor.getId());
        return ResponseEntity
                .status(200)
                .body(new ApiResponse<>(200, "Login successful",responseData));
    }

    public ResponseEntity<Object> updateVendorProfile(MultipartFile photoFile, MultipartFile[] sampleWorkFiles, VendorProfileRequest vendorProfile)  {
        if((photoFile != null && !vendorProfile.getProfileImageData().getPublicId().isEmpty()) ||
                (!vendorProfile.getProfileImageData().getPublicId().isEmpty() && vendorProfile.getProfileImageData().getUrl().isEmpty())){
            // delete the image from cloud
            try{
                Map deleteFileResult = cloudinaryService.deleteSingleFile(vendorProfile.getProfileImageData().getPublicId());
                if(deleteFileResult.get("result").equals("ok")){
                    vendorProfile.setProfileImageData(new ImageData());
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Vendor presentUser = vendorRepo.findByEmail(authentication.getName());
            if(presentUser == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            }
            presentUser.setGender(vendorProfile.getGender());
            presentUser.setAddress(vendorProfile.getAddress());
            presentUser.setFullName(vendorProfile.getFullName());
            presentUser.setPhoneNo(vendorProfile.getPhoneNo());
            presentUser.setServices(vendorProfile.getServices());
            presentUser.setBusinessType(vendorProfile.getBusinessType());
            presentUser.setSampleWork(vendorProfile.getSampleWorkData());

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
                presentUser.setProfileImageData(profileImageData);
            }else{
                presentUser.setProfileImageData(ImageData.builder()
                        .url(vendorProfile.getProfileImageData().getUrl())
                        .publicId(vendorProfile.getProfileImageData().getPublicId())
                        .build());
            }

            if( sampleWorkFiles != null && sampleWorkFiles.length > 0 ){
                List<Map> result = null;
                try{
                    result =  cloudinaryService.uploadMultipleFile(sampleWorkFiles);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                List<ImageData> sampleWork = new ArrayList<>(vendorProfile.getSampleWorkData());

                for(Map res: result){
                    ImageData imageData = new ImageData();
                    imageData.setUrl(res.get("secure_url").toString());
                    imageData.setPublicId(res.get("public_id").toString());
                    sampleWork.add(imageData);
                }
                presentUser.setSampleWork(sampleWork);
            }
            Vendor savedUser = vendorRepo.save(presentUser);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse(HttpStatus.OK.value(),
                            "Profile Updated Successfully",
                            VendorProfileResponse.builder()
                                    .fullName(savedUser.getFullName())
                                    .profileImageData(savedUser.getProfileImageData())
                                    .address(savedUser.getAddress())
                                    .gender(savedUser.getGender())
                                    .phoneNo(savedUser.getPhoneNo())
                                    .services(savedUser.getServices())
                                    .businessType(savedUser.getBusinessType())
                                    .sampleWorkData(savedUser.getSampleWork())
                                    .build()
                    ));
    }

    public ResponseEntity<Object> getProfileData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Vendor vendor = vendorRepo.findByEmail(email);

        if(vendor == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(),"successful", VendorProfileResponse.builder()
                        .fullName(vendor.getFullName())
                        .services(vendor.getServices())
                        .address(vendor.getAddress())
                        .profileImageData(vendor.getProfileImageData())
                        .phoneNo(vendor.getPhoneNo())
                        .gender(vendor.getGender())
                        .sampleWorkData(vendor.getSampleWork())
                        .businessType(vendor.getBusinessType())
                        .build())
                );
    }

    public ResponseEntity<Object> getVendors(String state, String city, String businessType) {
        List<VendorProfileResponse> res = vendorRepoImpl.getVendors(state,city,businessType);
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

    public ResponseEntity<Object> getVendorsByIds(ArrayList<String> vendorIds)  {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .message("successful")
                        .status(HttpStatus.OK.value())
                        .data(vendorRepoImpl.getVendorsByIds(vendorIds))
                        .build());
    }


    public ResponseEntity<Object> updateBookingRequestStatusForVendor(BookingRequestStatusObjForVendor obj) {
        UpdateResult res = eventBookingRequestRepoImpl.updateBookingRequestStatusForVendor(obj);
        if(! res.wasAcknowledged()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in updating booking request");
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("successfull")
                        .build());
    }

    public ResponseEntity<Object> getBookingRequestsForVendor(String vendorId) {
        List <EventBookingRequest> res = eventBookingRequestRepoImpl.getBookingRequestsForVendor(vendorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .data(res)
                        .build());
    }

    public ResponseEntity<Object> updateServiceRequestStatusForVendor(ServiceRequestStatusObjForVendor obj) {
        UpdateResult res = serviceBookingRequestRepoImpl.updateServiceRequestStatusForVendor(obj);
        if(! res.wasAcknowledged()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in updating booking request");
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message("successfull")
                        .build());
    }
}

