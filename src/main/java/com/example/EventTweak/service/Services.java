package com.example.EventTweak.service;

import com.example.EventTweak.model.EventBooking;
import com.example.EventTweak.model.ServiceBooking;
import com.example.EventTweak.model.ServiceBookingRequest;
import com.example.EventTweak.model.VendorBookingRequestObject;
import com.example.EventTweak.model.io.user.ApiResponse;
import com.example.EventTweak.repository.ServiceBookingRepo;
import com.example.EventTweak.repository.ServiceBookingRequestRepo;
import com.example.EventTweak.repository.ServiceBookingRequestRepoImpl;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class Services {
    private final ServiceBookingRepo serviceBookingRepo;
    private final ServiceBookingRequestRepo serviceBookingRequestRepo;
    private final ServiceBookingRequestRepoImpl serviceBookingRequestRepoImpl;
    public Services(ServiceBookingRepo serviceBookingRepo, ServiceBookingRequestRepo serviceBookingRequestRepo, ServiceBookingRequestRepoImpl serviceBookingRequestRepoImpl) {
        this.serviceBookingRepo = serviceBookingRepo;
        this.serviceBookingRequestRepo = serviceBookingRequestRepo;
        this.serviceBookingRequestRepoImpl = serviceBookingRequestRepoImpl;
    }

    public ResponseEntity<Object> requestVendorService(ServiceBookingRequest obj){
       ServiceBookingRequest res =  serviceBookingRequestRepo.save(obj);
       if(res == null){
           throw new ResponseStatusException(HttpStatus.OK,"Error while saving service booking request");
       }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .build());
    }

    public ResponseEntity<Object> getAllServicesRequestsForClient(String clientId) {
        List<ServiceBookingRequest> res =  serviceBookingRequestRepoImpl.getAllServicesRequestsForClient(clientId);

        if(res == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error in fetching service requests");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .data(res)
                        .build());
    }

    public ResponseEntity<Object> updateServiceRequestForClient(String serviceBookingRequestId, VendorBookingRequestObject vendorObj) {
        UpdateResult res = serviceBookingRequestRepoImpl.updateServiceRequestForClient(serviceBookingRequestId, vendorObj);
        if(!res.wasAcknowledged()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in updating vendor");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .build());
    }

    public ResponseEntity<Object> getAllServiceRequestsForVendor(String vendorId) {
        List<ServiceBookingRequest> res =  serviceBookingRequestRepoImpl.getAllServiceRequestsForVendor(vendorId);

        if(res == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error in fetching service requests");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .data(res)
                        .build());
    }

    public ResponseEntity<Object> saveServiceBooking(ServiceBooking obj) {
        ServiceBooking res = serviceBookingRepo.save(obj);
        if(res == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error while saving service");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .message("successful")
                        .status(HttpStatus.OK.value())
                        .build());

    }

    public ResponseEntity<Object> deleteServiceRequest(String serviceRequestId) {
        DeleteResult res =  serviceBookingRequestRepoImpl.deleteServiceRequest(serviceRequestId);

        if(!res.wasAcknowledged()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error in deleting service requests");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .data(res)
                        .build());
    }

    public ResponseEntity<Object> getAllServiceBookingsForClient(String clientId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message("successful")
                        .data(serviceBookingRequestRepoImpl.getAllServiceBookingsForClient(clientId))
                        .build());
    }

    public ResponseEntity<Object> getServiceBookingsForVendor(String vendorId) {
        List<ServiceBooking> res = serviceBookingRequestRepoImpl.getServiceBookingsForVendor(vendorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.builder()
                        .status(HttpStatus.OK.value())
                        .data(res)
                        .message("successful")
                        .build()
                );
    }

    public ResponseEntity<Object> cancelServiceBooking(String serviceBookingId) {
       DeleteResult res = serviceBookingRequestRepoImpl.cancelServiceBooking(serviceBookingId);
       if(!res.wasAcknowledged()){
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in deleting service booking");
       }
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(ApiResponse
                       .builder()
                       .message("successful")
                       .status(HttpStatus.OK.value())
                       .data(res)
                       .build());
    }
}
