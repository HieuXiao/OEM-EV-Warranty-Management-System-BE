package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ cho WarrantyClaim.
 * - Tạo, cập nhật, xóa
 * - Tự động set trạng thái CHECK khi tạo
 * - Response rút gọn (VIN, AccountId, partNumber, fileId)
 */
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

    /**
     * Tạo mới WarrantyClaim (status mặc định CHECK)
     */
    public WarrantyClaimResponse create(WarrantyClaimRequest request) {
        WarrantyClaim claim = new WarrantyClaim();

        claim.setClaimId(request.getClaimId());
        claim.setClaimDate(request.getClaimDate() != null ? request.getClaimDate() : LocalDate.now());
        claim.setDescription(request.getDescription());
        claim.setStatus(WarrantyClaimStatus.CHECK);

        // --- Vehicle ---
        Vehicle vehicle = vehicleRepository.findById(request.getVin())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe với VIN: " + request.getVin()));
        claim.setVehicle(vehicle);

        // --- Staff & Technician ---
        if (request.getScStaffId() != null) {
            Account staff = accountRepository.findById(request.getScStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy SC Staff: " + request.getScStaffId()));
            claim.setServiceCenterStaff(staff);
        }

        if (request.getScTechnicianId() != null) {
            Account tech = accountRepository.findById(request.getScTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Technician: " + request.getScTechnicianId()));
            claim.setServiceCenterTechnician(tech);
        }

        // --- Campaigns ---
        if (request.getCampaignIds() != null && !request.getCampaignIds().isEmpty()) {
            List<Campaign> campaigns = campaignRepository.findAllById(request.getCampaignIds());
            if (campaigns.isEmpty()) {
                throw new ResourceNotFoundException("Không tìm thấy campaign hợp lệ.");
            }

            // Lấy model của xe
            String vehicleModel = vehicle.getModel();

            // Kiểm tra: model của xe phải nằm trong model list của ÍT NHẤT MỘT campaign
            boolean valid = campaigns.stream()
                    .anyMatch(c -> c.getModel().contains(vehicleModel));

            if (!valid) {
                throw new IllegalArgumentException("Xe có model '" + vehicleModel +
                        "' không nằm trong danh sách model của bất kỳ campaign nào được chọn.");
            }

            claim.setCampaigns(campaigns);
        }

        WarrantyClaim saved = warrantyClaimRepository.save(claim);
        return mapToResponse(saved);
    }


    /**
     * Cập nhật claim: chỉ sửa mô tả, ngày, quan hệ
     */
    public WarrantyClaimResponse update(String id, WarrantyClaimRequest request) {
        WarrantyClaim existing = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim: " + id));

        if (request.getClaimDate() != null)
            existing.setClaimDate(request.getClaimDate());

        if (request.getDescription() != null)
            existing.setDescription(request.getDescription());

        if (request.getVin() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe: " + request.getVin()));
            existing.setVehicle(vehicle);
        }

        if (request.getScStaffId() != null) {
            Account staff = accountRepository.findById(request.getScStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy SC Staff: " + request.getScStaffId()));
            existing.setServiceCenterStaff(staff);
        }

        if (request.getScTechnicianId() != null) {
            Account tech = accountRepository.findById(request.getScTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Technician: " + request.getScTechnicianId()));
            existing.setServiceCenterTechnician(tech);
        }

        WarrantyClaim updated = warrantyClaimRepository.save(existing);
        return mapToResponse(updated);
    }

    /**
     * Xóa claim theo ID
     */
    public void delete(String id) {
        if (!warrantyClaimRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy WarrantyClaim: " + id);
        }
        warrantyClaimRepository.deleteById(id);
    }

    /**
     * Lấy tất cả claims
     */
    public List<WarrantyClaimResponse> getAll() {
        return warrantyClaimRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy claim theo ID
     */
    public WarrantyClaimResponse getById(String id) {
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim: " + id));
        return mapToResponse(claim);
    }

    /**
     * Lọc theo ngày yêu cầu
     */
    public List<WarrantyClaimResponse> getByClaimDate(LocalDate date) {
        return warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getClaimDate().equals(date))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lọc theo trạng thái
     */
    public List<WarrantyClaimResponse> getByStatus(String status) {
        return warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getStatus().name().equalsIgnoreCase(status))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Chuyển entity sang response (rút gọn)
     */
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

        return res;
    }
}
