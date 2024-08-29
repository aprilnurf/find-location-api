package com.location.find.model.outbound;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Contact {
    private List<Phone> phone;
    private List<Fax> fax;
    private List<Website> www;
    private List<Email> email;

}

