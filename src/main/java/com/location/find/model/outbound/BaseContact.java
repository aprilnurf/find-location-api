package com.location.find.model.outbound;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BaseContact {
    private String value;
    private List<Category> categories;
}
