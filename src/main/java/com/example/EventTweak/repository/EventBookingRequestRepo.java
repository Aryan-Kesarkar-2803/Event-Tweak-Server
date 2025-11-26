package com.example.EventTweak.repository;

import com.example.EventTweak.model.EventBookingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventBookingRequestRepo extends MongoRepository<EventBookingRequest, String> {
    List<EventBookingRequest> findByClientId(String clientId);
}
