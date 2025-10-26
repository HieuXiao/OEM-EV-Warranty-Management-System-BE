package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
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
        WarrantyClaimResponse response = modelMapper.map(saved, WarrantyClaimResponse.class);
        response.setStatus(saved.getStatus().name());
        return response;
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
        WarrantyClaimResponse response = modelMapper.map(updated, WarrantyClaimResponse.class);
        response.setStatus(updated.getStatus().name());
        return response;
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
     * Lấy tất cả claims
     */
    public List<WarrantyClaimResponse> getAll() {
        return warrantyClaimRepository.findAll().stream()
                .map(c -> {
                    WarrantyClaimResponse r = modelMapper.map(c, WarrantyClaimResponse.class);
                    r.setStatus(c.getStatus().name());
                    return r;
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy claim theo ID
     */
    public WarrantyClaimResponse getById(String id) {
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found: " + id));
        WarrantyClaimResponse response = modelMapper.map(claim, WarrantyClaimResponse.class);
        response.setStatus(claim.getStatus().name());
        return response;
    }

    /**
     * Lọc theo ngày yêu cầu chính xác
     */
    public List<WarrantyClaimResponse> getByClaimDate(LocalDate date) {
        return warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getClaimDate().equals(date))
                .map(c -> {
                    WarrantyClaimResponse r = modelMapper.map(c, WarrantyClaimResponse.class);
                    r.setStatus(c.getStatus().name());
                    return r;
                })
                .collect(Collectors.toList());
    }

    /**
     * Lọc theo trạng thái
     */
    public List<WarrantyClaimResponse> getByStatus(String status) {
        return warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getStatus().name().equalsIgnoreCase(status))
                .map(c -> {
                    WarrantyClaimResponse r = modelMapper.map(c, WarrantyClaimResponse.class);
                    r.setStatus(c.getStatus().name());
                    return r;
                })
                .collect(Collectors.toList());
    }
}
