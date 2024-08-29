package com.location.find.model.outbound;

import com.location.find.model.Address;
import com.location.find.model.Payment;
import com.location.find.model.Position;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Item {
    private String title;
    private String id;
    private String language;
    private String ontologyId;
    private String resultType;
    private Address address;
    private Position position;
    private List<Position> access;
    private double distance;
    private List<Category> categories;
    private List<Reference> references;
    private List<FoodType> foodTypes;
    private List<Contact> contacts;
    private List<OpeningHour> openingHours;
    private Payment payment;
}

