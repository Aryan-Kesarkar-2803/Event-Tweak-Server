package com.example.EventTweak.repository;

import com.example.EventTweak.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {

    User findByEmail(String email);
}
