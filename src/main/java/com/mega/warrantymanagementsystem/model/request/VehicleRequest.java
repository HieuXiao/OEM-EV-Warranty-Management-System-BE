package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
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
