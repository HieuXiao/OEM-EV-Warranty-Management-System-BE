package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

@Data
public class VehicleResponse {
    private String vin;
    private String plate;
    private String type;
    private String color;
    private String model;
    private CampaignResponse campaign;
    private CustomerResponse customer;
}
