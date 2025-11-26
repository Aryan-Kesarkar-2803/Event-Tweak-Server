package com.example.EventTweak.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String houseNo;
    private String locality;
    private String area;
    private String city;
    private String state;
    private String pinCode;
}
