package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WarrantyClaimResponse {
    private int claimId;
    private LocalDate claimDate;
    private String status;
    private String description;
    private String evmDescription;
    private boolean technicianDone;
    private boolean scStaffDone;

    private VehicleResponse vehicle;
    private AccountResponse serviceCenterStaff;
    private AccountResponse serviceCenterTechnician;
    private AccountResponse evm;
    private List<ClaimPartCheckResponse> claimPartChecks;
    private List<WarrantyFileResponse> warrantyFiles;
}
