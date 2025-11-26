package com.example.EventTweak.repository;

import com.example.EventTweak.model.Vendor;
import com.example.EventTweak.model.Venue;
import com.example.EventTweak.model.io.user.VendorProfileResponse;
import com.example.EventTweak.model.io.user.VendorResponse;
import com.example.EventTweak.model.io.user.VenueProfileResponse;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class VendorRepoImpl {

    private MongoTemplate mongoTemplate;

    VendorRepoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    public List<VendorProfileResponse> getVendors(String state, String city, String businessType){
        Query query = new Query();
        query.addCriteria(Criteria.where("address.state").is(state).and("address.city").is(city).and("businessType").is(businessType));
        List<Vendor> vendors = mongoTemplate.find(query,Vendor.class);

        List<VendorProfileResponse> res = new ArrayList<>();

        for(Vendor vendor: vendors){
            VendorProfileResponse temp = new VendorProfileResponse();
            temp.setAddress(vendor.getAddress());
            temp.setGender(vendor.getGender());
            temp.setBusinessType(vendor.getBusinessType());
            temp.setFullName(vendor.getFullName());
            temp.setServices(vendor.getServices());
            temp.setPhoneNo(vendor.getPhoneNo());
            temp.setProfileImageData(vendor.getProfileImageData());
            temp.setSampleWorkData(vendor.getSampleWork());
            temp.setId(vendor.getId());
            res.add(temp);
        }

        return res;
    }

    public List<VendorProfileResponse> getVendorsByIds (ArrayList<String> vendorIds){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(vendorIds));
        List<Vendor> vendors = mongoTemplate.find(query,Vendor.class);

        List<VendorProfileResponse> res = new ArrayList<>();

        for(Vendor vendor: vendors){
            VendorProfileResponse temp = new VendorProfileResponse();
            temp.setServices(vendor.getServices());
            temp.setAddress(vendor.getAddress());
            temp.setPhoneNo(vendor.getPhoneNo());
            temp.setFullName(vendor.getFullName());
            temp.setBusinessType(vendor.getBusinessType());
            temp.setProfileImageData(vendor.getProfileImageData());
            temp.setId(vendor.getId());
            temp.setSampleWorkData(vendor.getSampleWork());

            res.add(temp);
        }
        return res;
    }
}
