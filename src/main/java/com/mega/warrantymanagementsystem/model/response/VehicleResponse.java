package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
public class VehicleResponse {

    private String vin;
    private int campaignId;
    private String plate;
    private String type;
    private String color;
    private String model;


    private CustomerResponse customer;
}