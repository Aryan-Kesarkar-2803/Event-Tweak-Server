package com.example.EventTweak.repository;

import com.example.EventTweak.model.ServiceBookingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceBookingRequestRepo extends MongoRepository<ServiceBookingRequest, String> {



}
