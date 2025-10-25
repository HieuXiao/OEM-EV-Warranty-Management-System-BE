package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class VehicleRequest {
    @NotEmpty
    private String vin;

    @NotEmpty
    private String plate;

    @NotEmpty
    private String type;

    @NotEmpty
    private String color;

    @NotEmpty
    private String model;

    private Integer campaignId;
    private Integer customerId;
}
