package com.example.EventTweak.repository;

import com.example.EventTweak.model.User;
import com.example.EventTweak.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepo extends MongoRepository<Vendor, String> {

    Vendor findByEmail(String email);
}
