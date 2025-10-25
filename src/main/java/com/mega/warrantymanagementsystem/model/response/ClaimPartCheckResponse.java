package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

@Data
public class ClaimPartCheckResponse {
    private String partSerialId;
    private int quantity;
    private boolean isRepair;
    private WarrantyClaimResponse warrantyClaim;
}
