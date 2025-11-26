package com.example.EventTweak.repository;

import com.example.EventTweak.model.EventBooking;
import com.example.EventTweak.model.ServiceBooking;
import com.example.EventTweak.model.ServiceBookingRequest;
import com.example.EventTweak.model.VendorBookingRequestObject;
import com.example.EventTweak.model.io.user.ServiceRequestStatusObjForVendor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceBookingRequestRepoImpl {
    private final MongoTemplate mongoTemplate;

    public ServiceBookingRequestRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<ServiceBookingRequest> getAllServicesRequestsForClient(String clientId){
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));
        return mongoTemplate.find(query, ServiceBookingRequest.class);
    }

    public UpdateResult updateServiceRequestForClient(String serviceBookingRequestId, VendorBookingRequestObject vendorObj) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(serviceBookingRequestId)));
        Update update = new Update();
        update.set("vendor",vendorObj);
        return mongoTemplate.updateFirst(query,update,ServiceBookingRequest.class);
    }

    public List<ServiceBookingRequest> getAllServiceRequestsForVendor(String vendorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("vendor._id").is(new ObjectId(vendorId)));
        return mongoTemplate.find(query, ServiceBookingRequest.class);
    }

    public UpdateResult updateServiceRequestStatusForVendor(ServiceRequestStatusObjForVendor obj) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(obj.getServiceRequestId())).and("vendor._id").is(new ObjectId(obj.getVendorId())));
        Update update = new Update();
        update.set("vendor.amount", obj.getAmount());
        update.set("vendor.vendorAccepted", obj.isVendorAccepted());
        update.set("vendor.vendorRejected",obj.isVendorRejected());
        return mongoTemplate.updateFirst(query,update, ServiceBookingRequest.class);
    }

    public DeleteResult deleteServiceRequest(String serviceRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(serviceRequestId)));
        return mongoTemplate.remove(query,ServiceBookingRequest.class);
    }

    public List<ServiceBooking> getAllServiceBookingsForClient(String clientId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId).and("isActive").is(true));
        return mongoTemplate.find(query, ServiceBooking.class);
    }

    public List<ServiceBooking> getServiceBookingsForVendor(String vendorId) {
        List<ServiceBooking> res = new ArrayList<>();
        Query query = new Query();
        query.addCriteria(Criteria.where("vendor._id").is(new ObjectId(vendorId)).and("isActive").is(true));
        res = mongoTemplate.find(query, ServiceBooking.class);
        return res;
    }

    public DeleteResult cancelServiceBooking(String serviceBookingId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(serviceBookingId)));
        return mongoTemplate.remove(query, ServiceBooking.class);
    }

    public void completeServiceBookings() {
        Query query = new Query();
        String today = LocalDate.now().toString();
        query.addCriteria(Criteria.where("date").lt(today));
        Update update = new Update();
        update.set("isActive",false);
        mongoTemplate.updateMulti(query,update, ServiceBooking.class);
    }
}
