package com.example.EventTweak.repository;

import com.example.EventTweak.model.EventBooking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventBookingRepo extends MongoRepository<EventBooking,String> {
    List<EventBooking> findAllByVenue(String venueId);

    List<EventBooking> findByDate(String date);

    List<EventBooking> findByClientId(String clientId);
}
