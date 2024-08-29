package com.location.find.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Payment {
    private List<PaymentMethod> methods;
}
