package com.mega.warrantymanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
public class VehicleRequest {
    private String vin;
    private String plate;
    private String type;
    private String color;
    private String model;
    private String customerPhone;
}
