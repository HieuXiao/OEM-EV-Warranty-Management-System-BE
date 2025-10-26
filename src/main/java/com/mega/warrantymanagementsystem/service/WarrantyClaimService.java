package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.*;
import com.mega.warrantymanagementsystem.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Xử lý nghiệp vụ cho WarrantyClaim.
 * - CRUD cơ bản
 * - Status mặc định CHECK khi tạo
 * - Không can thiệp status thủ công (status cập nhật qua luồng chính)
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
    private ModelMapper modelMapper;

    /**
     * Tạo mới WarrantyClaim (status mặc định CHECK)
     */
    public WarrantyClaimResponse create(WarrantyClaimRequest request) {
        WarrantyClaim claim = new WarrantyClaim();

        claim.setClaimId(request.getClaimId());
        claim.setClaimDate(request.getClaimDate() != null ? request.getClaimDate() : LocalDate.now());
        claim.setDescription(request.getDescription());
        claim.setStatus(WarrantyClaimStatus.CHECK); // mặc định

        // Quan hệ Vehicle
        Vehicle vehicle = vehicleRepository.findById(request.getVin())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVin()));
        claim.setVehicle(vehicle);

        // Quan hệ Staff
        if (request.getScStaffId() != null) {
            Account scStaff = accountRepository.findById(request.getScStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("SC Staff not found: " + request.getScStaffId()));
            claim.setServiceCenterStaff(scStaff);
        }

        // Quan hệ Technician
        if (request.getScTechnicianId() != null) {
            Account scTech = accountRepository.findById(request.getScTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found: " + request.getScTechnicianId()));
            claim.setServiceCenterTechnician(scTech);
        }

        WarrantyClaim saved = warrantyClaimRepository.save(claim);
        return mapToResponse(saved);
    }

    /**
     * Cập nhật claim (chỉ cho phép sửa thông tin mô tả, ngày, quan hệ)
     */
    public WarrantyClaimResponse update(String id, WarrantyClaimRequest request) {
        WarrantyClaim existing = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found: " + id));

        if (request.getClaimDate() != null)
            existing.setClaimDate(request.getClaimDate());

        if (request.getDescription() != null)
            existing.setDescription(request.getDescription());

        if (request.getVin() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVin()));
            existing.setVehicle(vehicle);
        }

        if (request.getScStaffId() != null) {
            Account scStaff = accountRepository.findById(request.getScStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("SC Staff not found: " + request.getScStaffId()));
            existing.setServiceCenterStaff(scStaff);
        }

        if (request.getScTechnicianId() != null) {
            Account scTech = accountRepository.findById(request.getScTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found: " + request.getScTechnicianId()));
            existing.setServiceCenterTechnician(scTech);
        }

        WarrantyClaim updated = warrantyClaimRepository.save(existing);
        return mapToResponse(updated);
    }

    /**
     * Xóa claim theo ID (chỉ xóa claim, không cascade sang bảng khác)
     */
    public void delete(String id) {
        if (!warrantyClaimRepository.existsById(id)) {
            throw new ResourceNotFoundException("WarrantyClaim not found: " + id);
        }
        warrantyClaimRepository.deleteById(id);
    }

    /**
     * Lấy tất cả claims (đã fix vòng lặp JSON)
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
                .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found: " + id));
        return mapToResponse(claim);
    }

    /**
     * Lọc theo ngày yêu cầu chính xác
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
     * Hàm map entity sang response, cắt vòng lặp và chỉ lấy thông tin cần thiết
     */
    private WarrantyClaimResponse mapToResponse(WarrantyClaim claim) {
        WarrantyClaimResponse r = modelMapper.map(claim, WarrantyClaimResponse.class);
        r.setStatus(claim.getStatus().name());

        // Map vehicle
        if (claim.getVehicle() != null) {
            r.setVehicle(modelMapper.map(claim.getVehicle(), VehicleResponse.class));
        }

        // Map accounts (staff, tech, evm)
        if (claim.getServiceCenterStaff() != null) {
            r.setServiceCenterStaff(modelMapper.map(claim.getServiceCenterStaff(), AccountResponse.class));
        }
        if (claim.getServiceCenterTechnician() != null) {
            r.setServiceCenterTechnician(modelMapper.map(claim.getServiceCenterTechnician(), AccountResponse.class));
        }
        if (claim.getEvm() != null) {
            r.setEvm(modelMapper.map(claim.getEvm(), AccountResponse.class));
        }

        // Map claimPartChecks
        if (claim.getClaimPartChecks() != null) {
            r.setClaimPartChecks(
                    claim.getClaimPartChecks().stream()
                            .map(p -> modelMapper.map(p, ClaimPartCheckResponse.class))
                            .collect(Collectors.toList())
            );
        }

        // Map warrantyFiles
        if (claim.getWarrantyFiles() != null) {
            r.setWarrantyFiles(
                    claim.getWarrantyFiles().stream()
                            .map(f -> modelMapper.map(f, WarrantyFileResponse.class))
                            .collect(Collectors.toList())
            );
        }

        return r;
    }
}
