package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

import java.util.List;

@Data
public class ClaimPartCheckResponse {
    private String partNumber;
    private int quantity;
    private Boolean isRepair;
    private List<String> partSerials;
    private String partId; // optional, link sang PartUnderWarranty


    //    private WarrantyClaimResponse warrantyClaim;
    private VehicleResponse vehicle;
}
