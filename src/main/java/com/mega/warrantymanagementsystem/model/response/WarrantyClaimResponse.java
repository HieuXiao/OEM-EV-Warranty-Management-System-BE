package com.mega.warrantymanagementsystem.model.response;

import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
public class WarrantyClaimResponse {

    private int claimId;

    private String vin; // từ Vehicle

    private String scStaffId;

    private String scTechnicianId;

    private String evmId;

    private int policyId;

    private LocalDate claimDate;

    private WarrantyClaimStatus status;

    private String description;

    // Liệt kê danh sách các part, attachment, record liên quan
    private List<ClaimReplacementPartResponse> claimReplacementParts;
    private List<ClaimAttachmentResponse> claimAttachments;
    private List<ServiceRecordResponse> serviceRecords;

}
