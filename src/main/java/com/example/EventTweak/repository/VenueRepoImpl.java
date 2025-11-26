package com.example.EventTweak.repository;

import com.example.EventTweak.model.Venue;
import com.example.EventTweak.model.io.user.VenueProfileResponse;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class VenueRepoImpl {
    private MongoTemplate mongoTemplate;

    public VenueRepoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List<VenueProfileResponse> getVenuesByStateAndCity(String state, String city){
        List <Venue> venues;
        List < VenueProfileResponse> response = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("address.state").is(state).and("address.city").is(city));
        venues =  mongoTemplate.find(query, Venue.class);
        for(Venue temp : venues){
            response.add(
                    VenueProfileResponse.builder()
                            .id(temp.getId())
                            .coordinatorName(temp.getCoordinatorName())
                            .email(temp.getEmail())
                            .phoneNo(temp.getPhoneNo())
                            .address(temp.getAddress())
                            .profileImageData(temp.getProfileImageData())
                            .guestCapacity(temp.getGuestCapacity())
                            .services(temp.getServices())
                            .sampleWorkData(temp.getSampleWork())
                            .venueName(temp.getVenueName())
                            .amenities(temp.getAmenities())
                            .completedBookings(temp.getCompletedBookings())
                            .currentBookings(temp.getCurrentBookings())
                            .chargesPerDay(temp.getChargesPerDay())
                            .build()
            );
        }
        return response;
    }
}
