package com.example.EventTweak.service;

import com.example.EventTweak.model.io.user.ApiResponse;
import com.example.EventTweak.repository.EventBookingRepoImpl;
import com.example.EventTweak.repository.ServiceBookingRequestRepoImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneralService {

    private final CloudinaryService cloudinaryService;
    private final EventBookingRepoImpl eventBookingRepoImpl;
    private final ServiceBookingRequestRepoImpl serviceBookingRequestRepoImpl;

    GeneralService(CloudinaryService cloudinaryService, EventBookingRepoImpl eventBookingRepoImpl, ServiceBookingRequestRepoImpl serviceBookingRequestRepoImpl){
        this.cloudinaryService = cloudinaryService;
        this.eventBookingRepoImpl = eventBookingRepoImpl;
        this.serviceBookingRequestRepoImpl = serviceBookingRequestRepoImpl;
    }

    public ResponseEntity<Object> deleteImageFromCloud(List<String> publicIds){
        try{
            for(String publicId: publicIds){
                cloudinaryService.deleteSingleFile(publicId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(HttpStatus.OK.value(),"succesful",null));
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void completeEventsAndServiceBooking(){
        eventBookingRepoImpl.completeEventBookings();
        serviceBookingRequestRepoImpl.completeServiceBookings();
    }
}
