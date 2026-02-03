package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    private String userName;
    private String password;
    private String email;
    private Long phoneNumber;
    private String address;
    private int pincode;
    private String city;
    private String country;
    private String state;
    private String userType;

}
