package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.*;
import com.mega.warrantymanagementsystem.repository.*;
import com.mega.warrantymanagementsystem.service.WarrantyClaimService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WarrantyClaimServiceImpl implements WarrantyClaimService {

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private ClaimAttachmentRepository claimAttachmentRepository;

    @Autowired
    private ClaimReplacementPartRepository claimReplacementPartRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private ServiceRecordRepository serviceRecordRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CampaignRepository campaignRepository;

    // ---------------- CREATE ----------------
    @Override
    public WarrantyClaimResponse createWarrantyClaim(WarrantyClaimRequest request) {

        Vehicle vehicle = vehicleRepository.findByVin(request.getVin());
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with VIN: " + request.getVin());
        }

        Account staff = accountRepository.findByAccountId(request.getScStaffId());
        if (staff == null) {
            throw new ResourceNotFoundException("Service Center Staff not found with ID: " + request.getScStaffId());
        }

        Account technician = accountRepository.findByAccountId(request.getScTechnicianId());
        if (technician == null) {
            throw new ResourceNotFoundException("Technician not found with ID: " + request.getScTechnicianId());
        }

        Account evm = accountRepository.findByAccountId(request.getEvmId());
        if (evm == null) {
            throw new ResourceNotFoundException("EVM not found with ID: " + request.getEvmId());
        }

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found with ID: " + request.getPolicyId()));

        WarrantyClaim claim = new WarrantyClaim();
        claim.setVehicle(vehicle);
        claim.setServiceCenterStaff(staff);
        claim.setServiceCenterTechnician(technician);
        claim.setEvm(evm);
        claim.setPolicy(policy);
        claim.setStatus(request.getStatus());
        claim.setDescription(request.getDescription());
        claim.setClaimDate(LocalDate.now()); // gán ngày tạo tự động

        WarrantyClaim saved = warrantyClaimRepository.save(claim);
        return mapToResponse(saved);
    }

    // ---------------- UPDATE ----------------
    @Override
    public WarrantyClaimResponse updateWarrantyClaim(int claimId, WarrantyClaimRequest request) {
        WarrantyClaim existing = warrantyClaimRepository.findByClaimId(claimId);
        if (existing == null) {
            throw new ResourceNotFoundException("WarrantyClaim not found with ID: " + claimId);
        }

        // Không cho phép sửa claimDate (ngày tạo). Chỉ update những trường hợp cần thiết.
        existing.setDescription(request.getDescription());
        existing.setStatus(request.getStatus());

        WarrantyClaim updated = warrantyClaimRepository.save(existing);
        return mapToResponse(updated);
    }

    // ---------------- FIND BY ID ----------------
    @Override
    public WarrantyClaimResponse findById(int claimId) {
        WarrantyClaim claim = warrantyClaimRepository.findByClaimId(claimId);
        if (claim == null) {
            throw new ResourceNotFoundException("WarrantyClaim not found with ID: " + claimId);
        }
        return mapToResponse(claim);
    }

    // ---------------- FIND ALL ----------------
    @Override
    public List<WarrantyClaimResponse> findAll() {
        List<WarrantyClaim> claims = warrantyClaimRepository.findAll();
        List<WarrantyClaimResponse> responses = new ArrayList<>();

        for (WarrantyClaim c : claims) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    // ---------------- FIND BY STAFF ----------------
    @Override
    public List<WarrantyClaimResponse> findByStaffId(String staffId) {
        List<WarrantyClaim> claims = warrantyClaimRepository.findByServiceCenterStaff_AccountId(staffId);
        List<WarrantyClaimResponse> responses = new ArrayList<>();

        for (WarrantyClaim c : claims) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    // ---------------- FIND BY TECHNICIAN ----------------
    @Override
    public List<WarrantyClaimResponse> findByTechnicianId(String technicianId) {
        List<WarrantyClaim> claims = warrantyClaimRepository.findByServiceCenterTechnician_AccountId(technicianId);
        List<WarrantyClaimResponse> responses = new ArrayList<>();

        for (WarrantyClaim c : claims) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    // ---------------- FIND BY EVM ----------------
    @Override
    public List<WarrantyClaimResponse> findByEvmId(String evmId) {
        List<WarrantyClaim> claims = warrantyClaimRepository.findByEvm_AccountId(evmId);
        List<WarrantyClaimResponse> responses = new ArrayList<>();

        for (WarrantyClaim c : claims) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    // ---------------- FIND BY POLICY ----------------
    @Override
    public List<WarrantyClaimResponse> findByPolicyId(int policyId) {
        List<WarrantyClaim> claims = warrantyClaimRepository.findByPolicy_PolicyId(policyId);
        List<WarrantyClaimResponse> responses = new ArrayList<>();

        for (WarrantyClaim c : claims) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    // ---------------- FIND BY DATE ----------------
    @Override
    public List<WarrantyClaimResponse> findByClaimDate(LocalDate claimDate) {
        List<WarrantyClaim> claims = warrantyClaimRepository.findByClaimDate(claimDate);
        List<WarrantyClaimResponse> responses = new ArrayList<>();

        for (WarrantyClaim c : claims) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    // ---------------- FIND BY STATUS ----------------
    @Override
    public List<WarrantyClaimResponse> findByStatus(WarrantyClaimStatus status) {
        List<WarrantyClaim> claims = warrantyClaimRepository.findByStatus(status);
        List<WarrantyClaimResponse> responses = new ArrayList<>();

        for (WarrantyClaim c : claims) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    // ---------------- FIND BY VIN ----------------
    @Override
    public List<WarrantyClaimResponse> findByVehicleVin(String vin) {
        List<WarrantyClaim> claims = warrantyClaimRepository.findByVehicle_Vin(vin);
        List<WarrantyClaimResponse> responses = new ArrayList<>();

        for (WarrantyClaim c : claims) {
            responses.add(mapToResponse(c));
        }
        return responses;
    }

    // ---------------- DELETE ----------------
    @Override
    public void deleteWarrantyClaim(int claimId) {
        WarrantyClaim existing = warrantyClaimRepository.findByClaimId(claimId);
        if (existing == null) {
            throw new ResourceNotFoundException("WarrantyClaim not found with ID: " + claimId);
        }
        warrantyClaimRepository.delete(existing);
    }

    // ---------------UPDATE STATUS------------
    @Override
    public WarrantyClaimResponse updateStatus(int claimId, WarrantyClaimStatus newStatus) {
        Optional<WarrantyClaim> optional = warrantyClaimRepository.findById(claimId);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("WarrantyClaim not found with ID: " + claimId);
        }

        WarrantyClaim claim = optional.get();
        claim.setStatus(newStatus);
        WarrantyClaim updated = warrantyClaimRepository.save(claim);

        return mapToResponse(updated);
    }

    // ---------------- ATTACH EXISTING ATTACHMENT ----------------
    @Override
    @Transactional
    public void addAttachmentToClaim(int claimId, int attachmentId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));

        ClaimAttachment attachment = claimAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with ID: " + attachmentId));

        if (attachment.getWarrantyClaim() != null) {
            throw new IllegalStateException("Attachment is already linked to another claim");
        }
        attachment.setWarrantyClaim(claim);
        claim.getClaimAttachments().add(attachment);
        claimAttachmentRepository.save(attachment);
    }

    // ---------------- DETACH ATTACHMENT ----------------
    @Override
    @Transactional
    public void removeAttachmentFromClaim(int claimId, int attachmentId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));

        ClaimAttachment attachment = claimAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found with ID: " + attachmentId));

        if (attachment.getWarrantyClaim() == null
                || attachment.getWarrantyClaim().getClaimId() != claim.getClaimId()) {
            throw new IllegalStateException("Attachment is not linked to this claim");
        }
//        attachment.setWarrantyClaim(null);
//        claim.getClaimAttachments().remove(attachment);
//        claimAttachmentRepository.save(attachment);
        attachment.setWarrantyClaim(null);
        claimAttachmentRepository.save(attachment);
    }

    // ---------------- ADD REPLACEMENT PART TO CLAIM ----------------
    @Override
    @Transactional
    public void addReplacementPartToClaim(int claimId, int partUserId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));

        ClaimReplacementPart replacementPart = claimReplacementPartRepository.findById(partUserId)
                .orElseThrow(() -> new ResourceNotFoundException("ReplacementPart not found with ID: " + partUserId));

        if (replacementPart.getWarrantyClaim() != null) {
            throw new IllegalStateException("ReplacementPart is already linked to another claim");
        }

        replacementPart.setWarrantyClaim(claim);
        claim.getClaimReplacementParts().add(replacementPart);
        claimReplacementPartRepository.save(replacementPart);
    }


    // ---------------- REMOVE REPLACEMENT PART FROM CLAIM ----------------
    @Override
    @Transactional
    public void removeReplacementPartFromClaim(int claimId, int partUserId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));

        ClaimReplacementPart replacementPart = claimReplacementPartRepository.findById(partUserId)
                .orElseThrow(() -> new ResourceNotFoundException("ReplacementPart not found with ID: " + partUserId));

        if (replacementPart.getWarrantyClaim() == null
                || replacementPart.getWarrantyClaim().getClaimId() != claim.getClaimId()) {
            throw new IllegalStateException("ReplacementPart is not linked to this claim");
        }

//        replacementPart.setWarrantyClaim(null);
//        claim.getClaimReplacementParts().remove(replacementPart);
//
//        claimReplacementPartRepository.save(replacementPart);
        replacementPart.setWarrantyClaim(null);
        claimReplacementPartRepository.save(replacementPart);
    }


    // ---------------- ADD RECORD (AN TOÀN) ----------------
    @Override
    @Transactional
    public void addServiceRecordToClaim(int claimId, int serviceRecordId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));

        ServiceRecord serviceRecord = serviceRecordRepository.findById(serviceRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceRecord not found with ID: " + serviceRecordId));

        // Nếu record đã gắn claim khác, bạn có thể kiểm tra / từ chối hoặc cho phép chuyển
        if (serviceRecord.getWarrantyClaim() != null) {
            // ví dụ: ném exception nếu đã gắn
            throw new IllegalStateException("ServiceRecord is already attached to a claim: "
                    + serviceRecord.getWarrantyClaim().getClaimId());
        }

        // set quan hệ hai chiều để Hibernate thấy consistent trong session
        serviceRecord.setWarrantyClaim(claim);

        // thêm vào list của claim để memory consistent (không bắt buộc nhưng tốt để tránh mismatch)
        claim.getServiceRecords().add(serviceRecord);

        // lưu record (hoặc có thể lưu claim) — trong cùng transaction
        serviceRecordRepository.save(serviceRecord);
    }

    // ---------------- REMOVE RECORD FROM CLAIM ----------------
    @Override
    @Transactional
    public void removeServiceRecordFromClaim(int claimId, int serviceRecordId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));

        ServiceRecord serviceRecord = serviceRecordRepository.findById(serviceRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceRecord not found with ID: " + serviceRecordId));

        if (serviceRecord.getWarrantyClaim() == null
                || serviceRecord.getWarrantyClaim().getClaimId() != claim.getClaimId()) {
            throw new IllegalStateException("ServiceRecord is not attached to the given claim");
        }

//        // detach 2 chiều
//        serviceRecord.setWarrantyClaim(null);
//        claim.getServiceRecords().remove(serviceRecord);
//
//        serviceRecordRepository.save(serviceRecord);
        serviceRecord.setWarrantyClaim(null);
        serviceRecordRepository.save(serviceRecord);
    }

    // ---------------- ADD CAMPAIGN TO CLAIM ----------------
    @Override
    @Transactional
    public void addCampaignToClaim(int claimId, int campaignId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty Claim not found with ID: " + claimId));

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        if (!claim.getCampaigns().contains(campaign)) {
            claim.getCampaigns().add(campaign);
            warrantyClaimRepository.save(claim);
        }
    }

    // ---------------- REMOVE CAMPAIGN FROM CLAIM ----------------
    @Override
    @Transactional
    public void removeCampaignFromClaim(int claimId, int campaignId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty Claim not found with ID: " + claimId));

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        if (claim.getCampaigns().contains(campaign)) {
            claim.getCampaigns().remove(campaign);
            warrantyClaimRepository.save(claim);
        }
    }



    // ---------------- MAPPER ----------------
    private WarrantyClaimResponse mapToResponse(WarrantyClaim claim) {
        WarrantyClaimResponse warrantyClaimResponse = new WarrantyClaimResponse();

        // ======== Map các thông tin cơ bản ======== //
        warrantyClaimResponse.setClaimId(claim.getClaimId());
        warrantyClaimResponse.setClaimDate(claim.getClaimDate());
        warrantyClaimResponse.setDescription(claim.getDescription());
        warrantyClaimResponse.setStatus(claim.getStatus());

        if (claim.getVehicle() != null) {
            warrantyClaimResponse.setVin(claim.getVehicle().getVin());
        }
        if (claim.getServiceCenterStaff() != null) {
            warrantyClaimResponse.setScStaffId(claim.getServiceCenterStaff().getAccountId());
        }
        if (claim.getServiceCenterTechnician() != null) {
            warrantyClaimResponse.setScTechnicianId(claim.getServiceCenterTechnician().getAccountId());
        }
        if (claim.getEvm() != null) {
            warrantyClaimResponse.setEvmId(claim.getEvm().getAccountId());
        }
        if (claim.getPolicy() != null) {
            warrantyClaimResponse.setPolicyId(claim.getPolicy().getPolicyId());
        }

        // ======== Map Service Records ======== //
        if (claim.getServiceRecords() != null && !claim.getServiceRecords().isEmpty()) {
            List<ServiceRecordResponse> serviceRecordResponses = new ArrayList<>();

            for (ServiceRecord record : claim.getServiceRecords()) {
                ServiceRecordResponse recordResponse = new ServiceRecordResponse();
                recordResponse.setRecordId(record.getRecordId());
                recordResponse.setResult(record.getResult());
                recordResponse.setStatus(record.getStatus());
                recordResponse.setDescription(record.getDescription());
                recordResponse.setServiceDate(record.getServiceDate());

                if (record.getVehicle() != null) {
                    recordResponse.setVin(record.getVehicle().getVin());
                }
                if (record.getServiceAppointment() != null) {
                    recordResponse.setServiceAppointmentId(record.getServiceAppointment().getAppointmentId());
                }
                if (record.getCampaign() != null) {
                    recordResponse.setCampaignId(record.getCampaign().getCampaignId());
                }
                if (record.getServiceCenter() != null) {
                    recordResponse.setServiceCenterId(record.getServiceCenter().getCenterId());
                }

                serviceRecordResponses.add(recordResponse);
            }
            warrantyClaimResponse.setServiceRecords(serviceRecordResponses);
        }

        // ======== Map Claim Attachments ======== //
        if (claim.getClaimAttachments() != null && !claim.getClaimAttachments().isEmpty()) {
            List<ClaimAttachmentResponse> claimAttachmentResponses = new ArrayList<>();

            for (ClaimAttachment attachment : claim.getClaimAttachments()) {
                ClaimAttachmentResponse attachmentResponse = new ClaimAttachmentResponse();
                attachmentResponse.setAttachmentId(attachment.getAttachmentId());
                attachmentResponse.setFileType(attachment.getFileType());
                attachmentResponse.setFilePath(attachment.getFilePath());

                attachmentResponse.setClaimId(
                        (attachment.getWarrantyClaim() != null)
                                ? attachment.getWarrantyClaim().getClaimId()
                                : null
                );

                claimAttachmentResponses.add(attachmentResponse);
            }
            warrantyClaimResponse.setClaimAttachments(claimAttachmentResponses);
        }

        // ======== Map Claim Replacement Parts ======== //
        if (claim.getClaimReplacementParts() != null && !claim.getClaimReplacementParts().isEmpty()) {
            List<ClaimReplacementPartResponse> claimReplacementPartResponses = new ArrayList<>();

            for (ClaimReplacementPart replacementPart : claim.getClaimReplacementParts()) {
                ClaimReplacementPartResponse partResponse = new ClaimReplacementPartResponse();
                partResponse.setPartUserId(replacementPart.getPartUserId());
                partResponse.setQuantity(replacementPart.getQuantity());
                partResponse.setReason(replacementPart.getReason());
                partResponse.setDescription(replacementPart.getDescription());

                partResponse.setPartId(
                        (replacementPart.getPart() != null)
                                ? replacementPart.getPart().getPartId()
                                : null
                );

                partResponse.setClaimId(
                        (replacementPart.getWarrantyClaim() != null)
                                ? replacementPart.getWarrantyClaim().getClaimId()
                                : null
                );

                claimReplacementPartResponses.add(partResponse);
            }
            warrantyClaimResponse.setClaimReplacementParts(claimReplacementPartResponses);
        }

        // ======== Map Campaigns (bổ sung mới) ======== //
        if (claim.getCampaigns() != null && !claim.getCampaigns().isEmpty()) {
            List<CampaignResponse> campaignResponses = new ArrayList<>();

            for (Campaign campaign : claim.getCampaigns()) {
                CampaignResponse campaignResponse = new CampaignResponse();
                campaignResponse.setCampaignId(campaign.getCampaignId());
                campaignResponse.setCampaignName(campaign.getCampaignName());
                campaignResponse.setServiceDescription(campaign.getServiceDescription());
                campaignResponse.setStartDate(campaign.getStartDate());
                campaignResponse.setEndDate(campaign.getEndDate());
                campaignResponses.add(campaignResponse);
            }

            warrantyClaimResponse.setCampaigns(campaignResponses);
        }

        return warrantyClaimResponse;
    }



}
