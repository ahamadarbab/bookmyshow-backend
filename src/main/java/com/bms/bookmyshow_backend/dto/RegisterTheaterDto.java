package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTheaterDto {

    private String theaterName;
    private String address;
    private int pincode;
    private String city;
    private String country;
    private String state;

}
