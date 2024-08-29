package com.location.find.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindResponse {

    private String name;
    private List<String> openingHours;
    private Address address;
    private double distance;
    private Position position;
    private Payment payment;
    private String contact;


}
