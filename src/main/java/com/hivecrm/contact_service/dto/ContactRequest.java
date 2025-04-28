package com.hivecrm.contact_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContactRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private List<String> tags;
    private String address;
    private String city;
    private String zipCode;
    private String gender;
    private String website;
}
