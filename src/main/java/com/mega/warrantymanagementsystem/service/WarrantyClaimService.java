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

    /**
     * Tạo mới WarrantyClaim (status mặc định CHECK)
     */
    public WarrantyClaimResponse create(WarrantyClaimRequest request) {
        WarrantyClaim claim = new WarrantyClaim();

        claim.setClaimId(request.getClaimId());
        claim.setClaimDate(request.getClaimDate() != null ? request.getClaimDate() : LocalDate.now());
        claim.setDescription(request.getDescription());
        claim.setStatus(WarrantyClaimStatus.CHECK); // Mặc định trạng thái khi tạo mới

        // Vehicle
        Vehicle vehicle = vehicleRepository.findById(request.getVin())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy xe với VIN: " + request.getVin()));
        claim.setVehicle(vehicle);

        // SC Staff
        if (request.getScStaffId() != null) {
            Account staff = accountRepository.findById(request.getScStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy SC Staff: " + request.getScStaffId()));
            claim.setServiceCenterStaff(staff);
        }

        // SC Technician
        if (request.getScTechnicianId() != null) {
            Account tech = accountRepository.findById(request.getScTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Technician: " + request.getScTechnicianId()));
            claim.setServiceCenterTechnician(tech);
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
        res.setStatus(claim.getStatus() != null ? claim.getStatus().name() : null);
        res.setDescription(claim.getDescription());
        res.setEvmDescription(claim.getEvmDescription());
        res.setTechnicianDone(claim.isTechnicianDone());
        res.setScStaffDone(claim.isScStaffDone());
        res.setIsRepair(claim.getIsRepair());

        // Vehicle chỉ lấy VIN
        if (claim.getVehicle() != null) {
            res.setVin(claim.getVehicle().getVin());
        }

        // Accounts chỉ lấy ID
        if (claim.getServiceCenterStaff() != null) {
            res.setServiceCenterStaffId(claim.getServiceCenterStaff().getAccountId());
        }
        if (claim.getServiceCenterTechnician() != null) {
            res.setServiceCenterTechnicianId(claim.getServiceCenterTechnician().getAccountId());
        }
        if (claim.getEvm() != null) {
            res.setEvmId(claim.getEvm().getAccountId());
        }

        // ClaimPartCheck — chỉ lấy partNumber
        if (claim.getClaimPartChecks() != null && !claim.getClaimPartChecks().isEmpty()) {
            res.setPartNumbers(
                    claim.getClaimPartChecks().stream()
                            .map(p -> p.getPartNumber())
                            .collect(Collectors.toList())
            );
        }

        // WarrantyFiles — chỉ lấy fileId
        if (claim.getWarrantyFiles() != null && !claim.getWarrantyFiles().isEmpty()) {
            res.setFileIds(
                    claim.getWarrantyFiles().stream()
                            .map(WarrantyFile::getFileId)
                            .collect(Collectors.toList())
            );
        }

        return res;
    }
}
