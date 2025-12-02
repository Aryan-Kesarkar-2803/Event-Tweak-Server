package com.example.EventTweak.service;

import com.cloudinary.Api;
import com.example.EventTweak.model.EventBooking;
import com.example.EventTweak.model.EventBookingRequest;
import com.example.EventTweak.model.VendorBookingRequestObject;
import com.example.EventTweak.model.io.user.ApiResponse;
import com.example.EventTweak.model.io.user.SaveEventRequestObj;
import com.example.EventTweak.model.io.user.UpdateVenueForBookingRequestObj;
import com.example.EventTweak.repository.EventBookingRepo;
import com.example.EventTweak.repository.EventBookingRepoImpl;
import com.example.EventTweak.repository.EventBookingRequestRepo;
import com.example.EventTweak.repository.EventBookingRequestRepoImpl;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventBookingService {

    private final EventBookingRepo eventBookingRepo;
    private final EventBookingRepoImpl eventBookingRepoImpl;
    private final EventBookingRequestRepo eventBookingRequestRepo;
    private final EventBookingRequestRepoImpl eventBookingRequestRepoImpl;

    public EventBookingService(EventBookingRepo eventBookingRepo, EventBookingRepoImpl eventBookingRepoImpl, EventBookingRequestRepo eventBookingRequestRepo, EventBookingRequestRepoImpl eventBookingRequestRepoImpl){
        this.eventBookingRepo = eventBookingRepo;
        this.eventBookingRepoImpl = eventBookingRepoImpl;
        this.eventBookingRequestRepo = eventBookingRequestRepo;
        this.eventBookingRequestRepoImpl = eventBookingRequestRepoImpl;
    }

    public ResponseEntity<Object> saveEvent(SaveEventRequestObj saveEventRequestObj) {
        EventBooking savedBooking = eventBookingRepo.save(saveEventRequestObj.getEventBooking());

        if(savedBooking == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error in Saving event data");
        }

        // delete the request from requests collection
        String idToDelete = saveEventRequestObj.getBookingRequestId();
        DeleteResult res = null;
        if(!idToDelete.isEmpty()){
            res = eventBookingRequestRepoImpl.deleteBookingRequest(idToDelete);
        }

        if( res == null  && !idToDelete.isEmpty() ){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error in deleting the booking request");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("Successful")
                        .build());


    }

    public ResponseEntity<Object> getEventsForVenue(String venueId) {
        List<EventBooking> res = eventBookingRepoImpl.getActiveEventsForVenue(venueId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .data(res)
                        .message("successful")
                        .build()
                );
    }

    public ResponseEntity<Object> getEventsForVendor(String vendorId) {

        List<EventBooking> res = eventBookingRepoImpl.getActiveEventsForVendor(vendorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .data(res)
                        .message("successful")
                        .build()
                );
    }

    public ResponseEntity<Object> saveEventBookingRequest(EventBookingRequest eventBookingRequest) {

        EventBookingRequest savedEventBooking = eventBookingRequestRepo.save(eventBookingRequest);
        if(savedEventBooking == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error in Saving event request data");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("Successful")
                        .build());
    }

    public ResponseEntity<Object> getAllEventBookingRequestsForClient(String clientId) {
        List<EventBookingRequest> res = eventBookingRequestRepo.findByClientId(clientId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message("sucessfull")
                                .data(res)
                                .build()
                );
    }

    public ResponseEntity<Object> rejectVendorByClient(String vendorId, String requestId) {
        UpdateResult res = eventBookingRequestRepoImpl.rejectVendorByClient(vendorId, requestId);
        if(!res.wasAcknowledged()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while rejecting vendor");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .build());
    }

    public ResponseEntity<Object> addVendorsToBookingRequest(List<VendorBookingRequestObject> vendorsList, String requestId) {
        UpdateResult res = eventBookingRequestRepoImpl.addVendorsToBookingRequest(vendorsList, requestId);

        if(!res.wasAcknowledged()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error while adding vendors to booking request");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .build());
    }

    public ResponseEntity<Object> updateVenueForBookingRequest(UpdateVenueForBookingRequestObj obj) {
        UpdateResult res = eventBookingRequestRepoImpl.updateVenueForBookingRequest(obj);
        if(!res.wasAcknowledged()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error while updating venue");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .data(res)
                        .build());
    }

    public ResponseEntity<Object> getAllEventBookingsForClient(String clientId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .data(eventBookingRepoImpl.getAllEventBookingsForClient(clientId))
                        .build());
    }


    public ResponseEntity<Object> cancelEventBooking(String eventBookingId) {
        DeleteResult res = eventBookingRepoImpl.cancelEventBooking(eventBookingId);
        if(!res.wasAcknowledged()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in deleting event booking");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .data(res)
                        .build());
    }
}
