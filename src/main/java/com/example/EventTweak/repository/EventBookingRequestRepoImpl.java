package com.example.EventTweak.repository;

import com.example.EventTweak.model.EventBookingRequest;
import com.example.EventTweak.model.VendorBookingRequestObject;
import com.example.EventTweak.model.io.user.BookingRequestStatusObjForVendor;
import com.example.EventTweak.model.io.user.BookingRequestStatusObjForVenue;
import com.example.EventTweak.model.io.user.UpdateVenueForBookingRequestObj;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.sun.tools.jconsole.JConsoleContext;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class EventBookingRequestRepoImpl {
    private MongoTemplate mongoTemplate;

    public EventBookingRequestRepoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List<EventBookingRequest> getBookingRequestsForVenue(String venueId){
        List<EventBookingRequest> res;
        Query query = new Query();
        query.addCriteria(Criteria.where("venue._id").is(venueId));
        res = mongoTemplate.find(query, EventBookingRequest.class);
        return res;
    }

    public List<EventBookingRequest> getBookingRequestsForVendor(String vendorId){
        List<EventBookingRequest> res;
        Query query = new Query();
        query.addCriteria(Criteria.where("selectedServices._id").is(vendorId));
        res = mongoTemplate.find(query, EventBookingRequest.class);
        return res;
    }


    public UpdateResult updateBookingRequestStatusForVenue(BookingRequestStatusObjForVenue obj) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(obj.getId())));
        Update update = new Update();
        update.set("venue.venueAccepted", obj.isVenueAccepted());
        update.set("venue.venueRejected", obj.isVenueRejected());
        return mongoTemplate.updateFirst(query,update, EventBookingRequest.class);
    }

    public UpdateResult updateBookingRequestStatusForVendor(BookingRequestStatusObjForVendor obj) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(obj.getRequestId()))
                .and("selectedServices._id").is(new ObjectId(obj.getVendorId())));
        Update update = new Update();
        update.set("selectedServices.$.amount", obj.getAmount());
        update.set("selectedServices.$.vendorAccepted", obj.isVendorAccepted());
        update.set("selectedServices.$.vendorRejected", obj.isVendorRejected());
        return mongoTemplate.updateFirst(query,update, EventBookingRequest.class);
    }

    public UpdateResult rejectVendorByClient(String vendorId, String requestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(requestId))
                .and("selectedServices._id").is(new ObjectId(vendorId)));
        Update update = new Update();
        update.set("selectedServices.$.userRejected", true);

        return mongoTemplate.updateFirst(query,update,EventBookingRequest.class);
    }

    public UpdateResult addVendorsToBookingRequest(List<VendorBookingRequestObject> vendorsList, String requestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(requestId)));
        Update update = new Update();
        update.push("selectedServices").each(vendorsList);
        return mongoTemplate.updateFirst(query,update,EventBookingRequest.class);
    }

    public UpdateResult updateVenueForBookingRequest(UpdateVenueForBookingRequestObj obj) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(obj.getBookingRequestId())));
        Update update = new Update();
        update.set("venue._id", new ObjectId(obj.getVenue()));
        update.set("venue.venueAccepted", false);
        update.set("venue.venueRejected", false);
        update.set("venueAddress", obj.getVenueAddress());

        return mongoTemplate.updateFirst(query, update, EventBookingRequest.class);
    }

    public DeleteResult deleteBookingRequest(String bookingRequestId){
        System.out.println("Booking request id - "+bookingRequestId);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(bookingRequestId)));
        DeleteResult res = mongoTemplate.remove(query, EventBookingRequest.class);
        System.out.println("Delete - "+res);
        return res;
    }
}
