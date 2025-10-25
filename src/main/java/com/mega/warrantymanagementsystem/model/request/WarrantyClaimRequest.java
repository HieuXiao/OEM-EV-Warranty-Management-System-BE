package com.mega.warrantymanagementsystem.model.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WarrantyClaimRequest {
    private String claimId;
    private String vin;
    private String scStaffId;
    private String scTechnicianId;
    private String evmId;
    private Integer policyId;
    private LocalDate claimDate;
    private String status;
    private String description;
    private String evmDescription;
}
