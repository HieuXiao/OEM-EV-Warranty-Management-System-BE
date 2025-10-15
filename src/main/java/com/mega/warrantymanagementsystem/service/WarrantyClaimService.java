package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.model.request.ClaimAttachmentRequest;
import com.mega.warrantymanagementsystem.model.request.ClaimReplacementPartRequest;
import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface WarrantyClaimService {

    WarrantyClaimResponse createWarrantyClaim(WarrantyClaimRequest request);
    WarrantyClaimResponse updateWarrantyClaim(int claimId, WarrantyClaimRequest request);
    WarrantyClaimResponse findById(int claimId);
    List<WarrantyClaimResponse> findAll();
    List<WarrantyClaimResponse> findByStaffId(String staffId);
    List<WarrantyClaimResponse> findByTechnicianId(String technicianId);
    List<WarrantyClaimResponse> findByEvmId(String evmId);
    List<WarrantyClaimResponse> findByPolicyId(int policyId);
    List<WarrantyClaimResponse> findByClaimDate(LocalDate claimDate);
    List<WarrantyClaimResponse> findByStatus(WarrantyClaimStatus status);
    List<WarrantyClaimResponse> findByVehicleVin(String vin);

    void deleteWarrantyClaim(int claimId);
    WarrantyClaimResponse updateStatus(int claimId, WarrantyClaimStatus newStatus);

    // ---- Attachment ----
    void addAttachmentToClaim(int claimId, int attachmentId);
    void removeAttachmentFromClaim(int claimId, int attachmentId);

    // ---- Replacement Part ----
    void addReplacementPartToClaim(int claimId, ClaimReplacementPartRequest request);

    // ---- Service Record ----
    void addServiceRecordToClaim(int claimId, int serviceRecordId);
    void removeServiceRecordFromClaim(int claimId, int serviceRecordId);
}
