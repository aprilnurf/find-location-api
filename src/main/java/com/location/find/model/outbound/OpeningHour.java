package com.location.find.model.outbound;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OpeningHour {
    private List<Category> categories;
    private List<String> text;
    private boolean isOpen;
    private List<StructuredOpeningHour> structured;

}

