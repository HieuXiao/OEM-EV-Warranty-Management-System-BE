//package com.mega.warrantymanagementsystem.service.impl;
//
//import com.mega.warrantymanagementsystem.entity.*;
//import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
//import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
//import com.mega.warrantymanagementsystem.model.response.*;
//import com.mega.warrantymanagementsystem.repository.*;
//import com.mega.warrantymanagementsystem.service.WarrantyClaimService;
//import jakarta.transaction.Transactional;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class WarrantyClaimServiceImpl implements WarrantyClaimService {
//
//    @Autowired
//    private WarrantyClaimRepository warrantyClaimRepository;
//    @Autowired
//    private VehicleRepository vehicleRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private PolicyRepository policyRepository;
//    @Autowired
//    private ClaimAttachmentRepository claimAttachmentRepository;
//    @Autowired
//    private ClaimReplacementPartRepository claimReplacementPartRepository;
//    @Autowired
//    private PartRepository partRepository;
//    @Autowired
//    private ServiceRecordRepository serviceRecordRepository;
//    @Autowired
//    private ModelMapper modelMapper;
//    @Autowired
//    private CampaignRepository campaignRepository;
//
//    // ---------------- CREATE ----------------
//    @Override
//    public WarrantyClaimResponse createWarrantyClaim(WarrantyClaimRequest request) {
//        Vehicle vehicle = vehicleRepository.findByVin(request.getVin());
//        if (vehicle == null) throw new ResourceNotFoundException("Vehicle not found");
//
//        Account staff = accountRepository.findByAccountId(request.getScStaffId());
//        if (staff == null) throw new ResourceNotFoundException("SC Staff not found");
//
//        Account technician = accountRepository.findByAccountId(request.getScTechnicianId());
//        if (technician == null) throw new ResourceNotFoundException("Technician not found");
//
//        Policy policy = policyRepository.findById(request.getPolicyId())
//                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));
//
//        WarrantyClaim claim = new WarrantyClaim();
//        claim.setVehicle(vehicle);
//        claim.setServiceCenterStaff(staff);
//        claim.setServiceCenterTechnician(technician);
//        claim.setPolicy(policy);
//        claim.setStatus(WarrantyClaimStatus.CHECK);
//        claim.setClaimDate(LocalDate.now());
//        claim.setDescription(request.getDescription());
//        claim.setTechnicianDone(false);
//        claim.setScStaffDone(false);
//
//        WarrantyClaim saved = warrantyClaimRepository.save(claim);
//        return mapToResponse(saved);
//    }
//
//    // ---------------- UPDATE ----------------
//    @Override
//    public WarrantyClaimResponse updateWarrantyClaim(int claimId, WarrantyClaimRequest request) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        claim.setDescription(request.getDescription());
//        WarrantyClaim updated = warrantyClaimRepository.save(claim);
//        return mapToResponse(updated);
//    }
//
//    // ---------------- FIND BY ID ----------------
//    @Override
//    public WarrantyClaimResponse findById(int claimId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        return mapToResponse(claim);
//    }
//
//    // ---------------- FIND ALL ----------------
//    @Override
//    public List<WarrantyClaimResponse> findAll() {
//        return warrantyClaimRepository.findAll()
//                .stream().map(this::mapToResponse).toList();
//    }
//
//    // ---------------- FIND BY DATE ----------------
//    @Override
//    public List<WarrantyClaimResponse> findByClaimDate(LocalDate claimDate) {
//        return warrantyClaimRepository.findByClaimDate(claimDate)
//                .stream().map(this::mapToResponse).toList();
//    }
//
//    // ---------------- FIND BY STATUS ----------------
//    @Override
//    public List<WarrantyClaimResponse> findByStatus(WarrantyClaimStatus status) {
//        return warrantyClaimRepository.findByStatus(status)
//                .stream().map(this::mapToResponse).toList();
//    }
//
//    // ---------------- FIND BY VIN ----------------
//    @Override
//    public List<WarrantyClaimResponse> findByVehicleVin(String vin) {
//        return warrantyClaimRepository.findByVehicle_Vin(vin)
//                .stream().map(this::mapToResponse).toList();
//    }
//
//    // ---------------- DELETE ----------------
//    @Override
//    public void deleteWarrantyClaim(int claimId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        warrantyClaimRepository.delete(claim);
//    }
//
//    // ---------------- ATTACHMENTS ----------------
//    @Override
//    @Transactional
//    public void addAttachmentToClaim(int claimId, int attachmentId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        WarrantyFile attach = claimAttachmentRepository.findById(attachmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));
//        attach.setWarrantyClaim(claim);
//        claimAttachmentRepository.save(attach);
//
//        if (claim.getStatus() == WarrantyClaimStatus.CHECK) {
//            claim.setStatus(WarrantyClaimStatus.DECIDE);
//            warrantyClaimRepository.save(claim);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void removeAttachmentFromClaim(int claimId, int attachmentId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        WarrantyFile attachment = claimAttachmentRepository.findById(attachmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));
//        if (attachment.getWarrantyClaim() == null
//                || attachment.getWarrantyClaim().getClaimId() != claim.getClaimId()) {
//            throw new BusinessLogicException("Attachment is not linked to this claim");
//        }
//        attachment.setWarrantyClaim(null);
//        claimAttachmentRepository.save(attachment);
//    }
//
//    // ---------------- REPLACEMENT PARTS ----------------
//    @Override
//    @Transactional
//    public void addReplacementPartToClaim(int claimId, int partUserId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        ClaimPartCheck part = claimReplacementPartRepository.findById(partUserId)
//                .orElseThrow(() -> new ResourceNotFoundException("Part not found"));
//        part.setWarrantyClaim(claim);
//        claimReplacementPartRepository.save(part);
//
//        if (claim.getStatus() == WarrantyClaimStatus.CHECK) {
//            claim.setStatus(WarrantyClaimStatus.DECIDE);
//            warrantyClaimRepository.save(claim);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void removeReplacementPartFromClaim(int claimId, int partUserId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        ClaimPartCheck replacementPart = claimReplacementPartRepository.findById(partUserId)
//                .orElseThrow(() -> new ResourceNotFoundException("ReplacementPart not found"));
//        if (replacementPart.getWarrantyClaim() == null
//                || replacementPart.getWarrantyClaim().getClaimId() != claim.getClaimId()) {
//            throw new BusinessLogicException("ReplacementPart is not linked to this claim");
//        }
//        replacementPart.setWarrantyClaim(null);
//        claimReplacementPartRepository.save(replacementPart);
//    }
//
//    // ---------------- SERVICE RECORDS ----------------
//    @Override
//    @Transactional
//    public void addServiceRecordToClaim(int claimId, int recordId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        ServiceRecord record = serviceRecordRepository.findById(recordId)
//                .orElseThrow(() -> new ResourceNotFoundException("ServiceRecord not found"));
//        record.setWarrantyClaim(claim);
//        serviceRecordRepository.save(record);
//
//        if (claim.getStatus() == WarrantyClaimStatus.CHECK) {
//            claim.setStatus(WarrantyClaimStatus.DECIDE);
//            warrantyClaimRepository.save(claim);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void removeServiceRecordFromClaim(int claimId, int serviceRecordId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        ServiceRecord serviceRecord = serviceRecordRepository.findById(serviceRecordId)
//                .orElseThrow(() -> new ResourceNotFoundException("ServiceRecord not found"));
//        if (serviceRecord.getWarrantyClaim() == null
//                || serviceRecord.getWarrantyClaim().getClaimId() != claim.getClaimId()) {
//            throw new BusinessLogicException("ServiceRecord is not attached to this claim");
//        }
//        serviceRecord.setWarrantyClaim(null);
//        serviceRecordRepository.save(serviceRecord);
//    }
//
//    // ---------------- CAMPAIGNS ----------------
//    @Override
//    @Transactional
//    public void addCampaignToClaim(int claimId, int campaignId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        Campaign campaign = campaignRepository.findById(campaignId)
//                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
//        if (!claim.getCampaigns().contains(campaign)) {
//            claim.getCampaigns().add(campaign);
//            warrantyClaimRepository.save(claim);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void removeCampaignFromClaim(int claimId, int campaignId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//        Campaign campaign = campaignRepository.findById(campaignId)
//                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));
//        if (claim.getCampaigns().contains(campaign)) {
//            claim.getCampaigns().remove(campaign);
//            warrantyClaimRepository.save(claim);
//        }
//    }
//
//    // ---------------- EVM + STATUS FLOW ----------------
//    @Override
//    @Transactional
//    public void assignEvmToClaim(int claimId, String evmId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//
//        boolean hasComponent = !claim.getWarrantyFiles().isEmpty()
//                || !claim.getClaimPartChecks().isEmpty()
//                || !claim.getServiceRecords().isEmpty();
//
//        if (!hasComponent)
//            throw new BusinessLogicException("Cannot assign EVM before adding parts or records");
//
//        Account evm = accountRepository.findByAccountId(evmId);
//        if (evm == null) throw new ResourceNotFoundException("EVM not found");
//
//        claim.setEvm(evm);
//        claim.setStatus(WarrantyClaimStatus.REPAIR);
//        warrantyClaimRepository.save(claim);
//    }
//
//    // ---------------- UPDATE EVM DESCRIPTION ----------------
//    @Override
//    @Transactional
//    public void updateEvmDescription(int claimId, String description) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//
//        if (claim.getEvm() == null)
//            throw new BusinessLogicException("Cannot update EVM description before assigning EVM");
//
//        claim.setEvmDescription(description);
//        warrantyClaimRepository.save(claim);
//    }
//
//    @Override
//    @Transactional
//    public void technicianToggleDone(int claimId, boolean done) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//
//        if (claim.getStatus() != WarrantyClaimStatus.REPAIR)
//            throw new BusinessLogicException("Technician can only toggle done in REPAIR state");
//
//        claim.setTechnicianDone(done);
//        claim.setStatus(done ? WarrantyClaimStatus.HANDOVER : WarrantyClaimStatus.REPAIR);
//        warrantyClaimRepository.save(claim);
//    }
//
//    @Override
//    @Transactional
//    public void scStaffToggleDone(int claimId, boolean done) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//
//        if (claim.getStatus() != WarrantyClaimStatus.HANDOVER)
//            throw new BusinessLogicException("SC Staff can only mark done after handover");
//
//        claim.setScStaffDone(done);
//        claim.setStatus(done ? WarrantyClaimStatus.DONE : WarrantyClaimStatus.HANDOVER);
//        warrantyClaimRepository.save(claim);
//    }
//
//    // ---------------- CUSTOM MAPPING ----------------
//    private WarrantyClaimResponse mapToResponse(WarrantyClaim claim) {
//        WarrantyClaimResponse response = modelMapper.map(claim, WarrantyClaimResponse.class);
//
//        if (claim.getVehicle() != null)
//            response.setVin(claim.getVehicle().getVin());
//
//        if (claim.getServiceCenterStaff() != null)
//            response.setScStaffId(claim.getServiceCenterStaff().getAccountId());
//
//        if (claim.getServiceCenterTechnician() != null)
//            response.setScTechnicianId(claim.getServiceCenterTechnician().getAccountId());
//
//        if (claim.getEvm() != null)
//            response.setEvmId(claim.getEvm().getAccountId());
//
//        return response;
//    }
//}
