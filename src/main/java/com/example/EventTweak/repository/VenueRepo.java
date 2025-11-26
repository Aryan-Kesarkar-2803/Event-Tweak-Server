package com.example.EventTweak.repository;

import com.example.EventTweak.model.Venue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepo extends MongoRepository<Venue, String> {
    Venue findByEmail(String email);
}
