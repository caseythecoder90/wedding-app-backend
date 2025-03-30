package com.wedding.model.request;

import lombok.Data;

@Data
public class GuestRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean plusOneAllowed;

}