package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ClaimAttachmentRequest;
import com.mega.warrantymanagementsystem.model.request.ClaimReplacementPartRequest;
import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.ServiceRecordResponse;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
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
        attachment.setWarrantyClaim(null);
        claim.getClaimAttachments().remove(attachment);
        claimAttachmentRepository.save(attachment);
    }

    // ---------------- ADD REPLACEMENT PART ----------------
    @Override
    public void addReplacementPartToClaim(int claimId, ClaimReplacementPartRequest request) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));

        Part part = partRepository.findById(request.getPartUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Part not found with ID: " + request.getPartUserId()));

        ClaimReplacementPart replacementPart = new ClaimReplacementPart();
        replacementPart.setWarrantyClaim(claim);
        replacementPart.setPart(part);

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

        // detach 2 chiều
        serviceRecord.setWarrantyClaim(null);
        claim.getServiceRecords().remove(serviceRecord);

        serviceRecordRepository.save(serviceRecord);
    }


    // ---------------- MAPPER ----------------
    private WarrantyClaimResponse mapToResponse(WarrantyClaim claim) {
        WarrantyClaimResponse response = modelMapper.map(claim, WarrantyClaimResponse.class);

        if (claim.getVehicle() != null) {
            response.setVin(claim.getVehicle().getVin());
        }
        if (claim.getServiceCenterStaff() != null) {
            response.setScStaffId(claim.getServiceCenterStaff().getAccountId());
        }
        if (claim.getServiceCenterTechnician() != null) {
            response.setScTechnicianId(claim.getServiceCenterTechnician().getAccountId());
        }
        if (claim.getEvm() != null) {
            response.setEvmId(claim.getEvm().getAccountId());
        }
        if (claim.getPolicy() != null) {
            response.setPolicyId(claim.getPolicy().getPolicyId());
        }

        if (claim.getServiceRecords() != null && !claim.getServiceRecords().isEmpty()) {
            List<ServiceRecordResponse> recordResponses = new ArrayList<>();
            for (ServiceRecord record : claim.getServiceRecords()) {
                ServiceRecordResponse recordRes = new ServiceRecordResponse();
                recordRes.setRecordId(record.getRecordId());
                recordRes.setResult(record.getResult());
                recordRes.setStatus(record.getStatus());
                recordRes.setDescription(record.getDescription());
                recordRes.setServiceDate(record.getServiceDate());

                if (record.getVehicle() != null) {
                    recordRes.setVin(record.getVehicle().getVin());
                }
                if (record.getServiceAppointment() != null) {
                    recordRes.setServiceAppointmentId(record.getServiceAppointment().getAppointmentId());
                }
                if (record.getCampaign() != null) {
                    recordRes.setCampaignId(record.getCampaign().getCampaignId());
                }
                if (record.getServiceCenter() != null) {
                    recordRes.setServiceCenterId(record.getServiceCenter().getCenterId());
                }

                recordResponses.add(recordRes);
            }
            response.setServiceRecords(recordResponses);
        }

        return response;
    }
}
