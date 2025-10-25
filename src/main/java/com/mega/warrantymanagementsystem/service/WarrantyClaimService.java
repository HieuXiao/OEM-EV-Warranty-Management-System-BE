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
 * - Search theo ngày, trạng thái
 * - Status mặc định là CHECK khi tạo mới
 */
@Service
public class WarrantyClaimService {

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Tạo mới WarrantyClaim (status mặc định là CHECK)
     */
    public WarrantyClaimResponse create(WarrantyClaimRequest request) {
        WarrantyClaim claim = new WarrantyClaim();

        claim.setClaimId(request.getClaimId());
        claim.setClaimDate(request.getClaimDate());
        claim.setDescription(request.getDescription());
        claim.setEvmDescription(request.getEvmDescription());

        // Mặc định status CHECK
        claim.setStatus(WarrantyClaimStatus.CHECK);

        // Map quan hệ
        claim.setVehicle(vehicleRepository.findById(request.getVin())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVin())));

        claim.setPolicy(policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found: " + request.getPolicyId())));

        if (request.getScStaffId() != null) {
            claim.setServiceCenterStaff(accountRepository.findById(request.getScStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("SC Staff not found: " + request.getScStaffId())));
        }

        if (request.getScTechnicianId() != null) {
            claim.setServiceCenterTechnician(accountRepository.findById(request.getScTechnicianId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found: " + request.getScTechnicianId())));
        }

        if (request.getEvmId() != null) {
            claim.setEvm(accountRepository.findById(request.getEvmId())
                    .orElseThrow(() -> new ResourceNotFoundException("EVM not found: " + request.getEvmId())));
        }

        WarrantyClaim saved = warrantyClaimRepository.save(claim);
        return modelMapper.map(saved, WarrantyClaimResponse.class);
    }

    /**
     * Cập nhật claim (không cho sửa status ở đây)
     */
    public WarrantyClaimResponse update(String id, WarrantyClaimRequest request) {
        WarrantyClaim existing = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found: " + id));

        existing.setClaimDate(request.getClaimDate());
        existing.setDescription(request.getDescription());
        existing.setEvmDescription(request.getEvmDescription());

        // Quan hệ có thể thay đổi (nếu cần)
        if (request.getPolicyId() != null) {
            existing.setPolicy(policyRepository.findById(request.getPolicyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Policy not found: " + request.getPolicyId())));
        }

        if (request.getVin() != null) {
            existing.setVehicle(vehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVin())));
        }

        WarrantyClaim updated = warrantyClaimRepository.save(existing);
        return modelMapper.map(updated, WarrantyClaimResponse.class);
    }

    /**
     * Xóa claim theo ID
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
                .map(c -> modelMapper.map(c, WarrantyClaimResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy claim theo ID
     */
    public WarrantyClaimResponse getById(String id) {
        WarrantyClaim claim = warrantyClaimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found: " + id));
        return modelMapper.map(claim, WarrantyClaimResponse.class);
    }

    /**
     * Lọc theo ngày yêu cầu chính xác
     */
    public List<WarrantyClaimResponse> getByClaimDate(LocalDate date) {
        return warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getClaimDate().equals(date))
                .map(c -> modelMapper.map(c, WarrantyClaimResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Lọc theo trạng thái
     */
    public List<WarrantyClaimResponse> getByStatus(String status) {
        return warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getStatus().name().equalsIgnoreCase(status))
                .map(c -> modelMapper.map(c, WarrantyClaimResponse.class))
                .collect(Collectors.toList());
    }
}
