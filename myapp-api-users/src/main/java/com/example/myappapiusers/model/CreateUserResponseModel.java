package com.example.myappapiusers.model;

import lombok.Data;

@Data
public class CreateUserResponseModel {
    private String firstName;
    private String lastName;
    private String email;
    private String userId;
    private String encryptedPassword;
}
