//package com.mega.warrantymanagementsystem.service;
//
//import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
//import com.mega.warrantymanagementsystem.model.request.ClaimAttachmentRequest;
//import com.mega.warrantymanagementsystem.model.request.ClaimReplacementPartRequest;
//import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
//import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public interface WarrantyClaimService {
//
//    WarrantyClaimResponse createWarrantyClaim(WarrantyClaimRequest request);
//    WarrantyClaimResponse updateWarrantyClaim(int claimId, WarrantyClaimRequest request);
//    WarrantyClaimResponse findById(int claimId);
//    List<WarrantyClaimResponse> findAll();
////    List<WarrantyClaimResponse> findByPolicyId(int policyId);
//    List<WarrantyClaimResponse> findByClaimDate(LocalDate claimDate);
//    List<WarrantyClaimResponse> findByStatus(WarrantyClaimStatus status);
//    List<WarrantyClaimResponse> findByVehicleVin(String vin);
//    void updateEvmDescription(int claimId, String description);
//    void deleteWarrantyClaim(int claimId);
//
//    // ---- Attachment ----
//    void addAttachmentToClaim(int claimId, int attachmentId);
//    void removeAttachmentFromClaim(int claimId, int attachmentId);
//
//    // ---- Replacement Part ----
//    void addReplacementPartToClaim(int claimId, int partUserId);
//    void removeReplacementPartFromClaim(int claimId, int partUserId);
//
//    // ---- Service Record ----
//    void addServiceRecordToClaim(int claimId, int serviceRecordId);
//    void removeServiceRecordFromClaim(int claimId, int serviceRecordId);
//
//    // ---- Campaign ----
//    void addCampaignToClaim(int claimId, int campaignId);
//    void removeCampaignFromClaim(int claimId, int campaignId);
//
//    // ----- Bo sung -----
//    void assignEvmToClaim(int claimId, String evmId);
//    void technicianToggleDone(int claimId, boolean done);
//    void scStaffToggleDone(int claimId, boolean done);
//
//}
