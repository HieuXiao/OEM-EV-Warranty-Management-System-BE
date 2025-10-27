package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

@Data
public class ClaimPartCheckResponse {
    private String partNumber;
    private int quantity;
    private Boolean isRepair;
    private String partSerial;

//    private WarrantyClaimResponse warrantyClaim;
    private VehicleResponse vehicle;
}
