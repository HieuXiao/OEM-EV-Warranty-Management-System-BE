package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarrantyClaimService {

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private WarrantyClaimProgressRepository progressRepository;

    // ---------------- CREATE ----------------
    public WarrantyClaimResponse create(WarrantyClaimRequest request) {
        if (request.getClaimId() == null || request.getClaimId().trim().isEmpty())
            throw new IllegalArgumentException("Claim ID cannot be empty.");
        if (request.getVin() == null || request.getVin().trim().isEmpty())
            throw new IllegalArgumentException("VIN cannot be empty.");
        if (request.getDescription() == null || request.getDescription().trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty.");

        if (warrantyClaimRepository.existsById(request.getClaimId()))
            throw new IllegalArgumentException("Claim ID already exists: " + request.getClaimId());

        WarrantyClaim claim = new WarrantyClaim();
        claim.setClaimId(request.getClaimId());
        claim.setClaimDate(request.getClaimDate() != null ? request.getClaimDate() : LocalDate.now());
        claim.setDescription(request.getDescription());
        claim.setStatus(WarrantyClaimStatus.CHECK);

        // Vehicle
        Vehicle vehicle = vehicleRepository.findById(request.getVin())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with VIN: " + request.getVin()));
        claim.setVehicle(vehicle);

        // Staff
        if (request.getScStaffId() != null) {
            Account staff = accountRepository.findById(request.getScStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + request.getScStaffId()));
            claim.setServiceCenterStaff(staff);
        }

        // Technician
        if (request.getScTechnicianId() != null) {
            Account tech = accountRepository.findById(request.getScTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found: " + request.getScTechnicianId()));
            claim.setServiceCenterTechnician(tech);
        }

        // Campaigns
        if (request.getCampaignIds() != null && !request.getCampaignIds().isEmpty()) {
            List<Campaign> campaigns = campaignRepository.findAllById(request.getCampaignIds());
            claim.setCampaigns(campaigns);
        }

        WarrantyClaim saved = warrantyClaimRepository.save(claim);

        // Log trạng thái CHECK
        WarrantyClaimProgress progress = new WarrantyClaimProgress();
        progress.setWarrantyClaim(saved);
        progress.setStatus(WarrantyClaimStatus.CHECK.name());
        progress.setTimestamp(LocalDateTime.now());
        progressRepository.save(progress);

        return mapToResponse(saved);
    }

    // ---------------- UPDATE ----------------
    public WarrantyClaimResponse update(String id, WarrantyClaimRequest request) {
        WarrantyClaim existing = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found: " + id));

        if (request.getClaimDate() != null)
            existing.setClaimDate(request.getClaimDate());

        if (request.getDescription() != null && !request.getDescription().trim().isEmpty())
            existing.setDescription(request.getDescription());

        if (request.getVin() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVin()));
            existing.setVehicle(vehicle);
        }

        if (request.getScStaffId() != null) {
            Account staff = accountRepository.findById(request.getScStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + request.getScStaffId()));
            existing.setServiceCenterStaff(staff);
        }

        if (request.getScTechnicianId() != null) {
            Account tech = accountRepository.findById(request.getScTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found: " + request.getScTechnicianId()));
            existing.setServiceCenterTechnician(tech);
        }

        WarrantyClaim updated = warrantyClaimRepository.save(existing);
        return mapToResponse(updated);
    }

    // ---------------- DELETE ----------------
    public void delete(String id) {
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found: " + id));
        warrantyClaimRepository.delete(claim);
    }

    // ---------------- GET ALL ----------------
    public List<WarrantyClaimResponse> getAll() {
        return warrantyClaimRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ---------------- GET BY ID ----------------
    public WarrantyClaimResponse getById(String id) {
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty claim not found: " + id));
        return mapToResponse(claim);
    }

    // ---------------- FILTER BY DATE ----------------
    public List<WarrantyClaimResponse> getByClaimDate(LocalDate date) {
        return warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getClaimDate().equals(date))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ---------------- FILTER BY STATUS ----------------
    public List<WarrantyClaimResponse> getByStatus(String status) {
        return warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getStatus().name().equalsIgnoreCase(status))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ---------------- MAP ENTITY → RESPONSE ----------------
    private WarrantyClaimResponse mapToResponse(WarrantyClaim claim) {
        WarrantyClaimResponse res = new WarrantyClaimResponse();
        res.setClaimId(claim.getClaimId());
        res.setClaimDate(claim.getClaimDate());
        res.setStatus(claim.getStatus().name());
        res.setDescription(claim.getDescription());
        res.setEvmDescription(claim.getEvmDescription());
        res.setTechnicianDone(claim.isTechnicianDone());
        res.setScStaffDone(claim.isScStaffDone());
        res.setIsRepair(claim.getIsRepair());

        if (claim.getVehicle() != null)
            res.setVin(claim.getVehicle().getVin());
        if (claim.getServiceCenterStaff() != null)
            res.setServiceCenterStaffId(claim.getServiceCenterStaff().getAccountId());
        if (claim.getServiceCenterTechnician() != null)
            res.setServiceCenterTechnicianId(claim.getServiceCenterTechnician().getAccountId());
        if (claim.getEvm() != null)
            res.setEvmId(claim.getEvm().getAccountId());
        if (claim.getClaimPartChecks() != null)
            res.setPartNumbers(claim.getClaimPartChecks().stream().map(ClaimPartCheck::getPartNumber).toList());
        if (claim.getWarrantyFiles() != null)
            res.setFileIds(claim.getWarrantyFiles().stream().map(WarrantyFile::getFileId).toList());
        if (claim.getCampaigns() != null)
            res.setCampaignIds(claim.getCampaigns().stream().map(Campaign::getCampaignId).toList());

        // Build timeline
        List<WarrantyClaimProgress> progressList =
                progressRepository.findByWarrantyClaim_ClaimIdOrderByTimestampAsc(claim.getClaimId());

        if (!progressList.isEmpty()) {
            List<String> timeline = new ArrayList<>();

            for (int i = 0; i < progressList.size(); i++) {
                WarrantyClaimProgress current = progressList.get(i);
                timeline.add(current.getStatus() + " : " + current.getTimestamp());

                if (i > 0) {
                    WarrantyClaimProgress prev = progressList.get(i - 1);
                    Duration diff = Duration.between(prev.getTimestamp(), current.getTimestamp());

                    long seconds = diff.getSeconds();
                    long hours = seconds / 3600;
                    long minutes = (seconds % 3600) / 60;
                    long remainingSeconds = seconds % 60;

                    String timeDiff;
                    if (hours > 0) timeDiff = hours + "h";
                    else if (minutes > 0) timeDiff = minutes + "m";
                    else timeDiff = remainingSeconds + "s";

                    timeline.add(prev.getStatus() + " -> " + current.getStatus() + " : " + timeDiff);
                }
            }

            res.setTimeline(timeline);
        }


        return res;
    }
}
