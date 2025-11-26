package com.example.EventTweak.repository;

import com.example.EventTweak.model.ServiceBooking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceBookingRepo extends MongoRepository<ServiceBooking,String> {

}
