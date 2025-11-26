package com.example.EventTweak.repository;

import com.example.EventTweak.model.EventBooking;
import com.mongodb.client.result.DeleteResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventBookingRepoImpl {

    private MongoTemplate mongoTemplate;

    public EventBookingRepoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List<EventBooking> getActiveEventsForVenue(String venueId){
        List<EventBooking> res = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("venue").is(venueId).and("isActive").is(true));
        res = mongoTemplate.find(query, EventBooking.class);
        return res;
    }

    public List<EventBooking> getActiveEventsForVendor(String vendorId) {
        List<EventBooking> res = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("services._id").is(new ObjectId(vendorId)).and("isActive").is(true));
        res = mongoTemplate.find(query, EventBooking.class);
        return res;
    }

    public List<EventBooking> getAllEventBookingsForClient(String clientId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));
        return mongoTemplate.find(query,EventBooking.class);
    }


    public DeleteResult cancelEventBooking(String eventBookingId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(eventBookingId)));
        return mongoTemplate.remove(query,EventBooking.class);
    }

    public void completeEventBookings(){
        Query query = new Query();
        String today = LocalDate.now().toString();
        query.addCriteria(Criteria.where("date").lt(today));
        Update update = new Update();
        update.set("isActive",false);
        mongoTemplate.updateMulti(query,update,EventBooking.class);
    }
}
