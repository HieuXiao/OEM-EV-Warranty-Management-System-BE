package com.mega.warrantymanagementsystem.service.v2;

import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Xử lý các hành động của Technician, Staff, EVM trong luồng trạng thái claim.
 */
@Service
public class ClaimWorkflowService {

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RepairPartService repairPartService;

    /**
     * Technician hoàn tất sửa chữa → chuyển REPAIR → HANDOVER
     */
    @Transactional
    public WarrantyClaimResponse technicianToggleDone(String claimId, String technicianId, boolean done) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found: " + claimId));

        if (claim.getServiceCenterTechnician() == null ||
                !claim.getServiceCenterTechnician().getAccountId().equalsIgnoreCase(technicianId)) {
            throw new BusinessLogicException("Technician không có quyền thao tác claim này.");
        }

        if (claim.getStatus() != WarrantyClaimStatus.REPAIR) {
            throw new BusinessLogicException("Technician chỉ có thể hoàn tất trong trạng thái REPAIR.");
        }

        claim.setTechnicianDone(done);
        if (done) claim.setStatus(WarrantyClaimStatus.HANDOVER);

        warrantyClaimRepository.save(claim);
        return mapToResponse(claim);
    }

    /**
     * Staff xác nhận bàn giao → chuyển HANDOVER → DONE
     */
    @Transactional
    public WarrantyClaimResponse scStaffToggleDone(String claimId, String staffId, boolean done) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found: " + claimId));

        if (claim.getServiceCenterStaff() == null ||
                !claim.getServiceCenterStaff().getAccountId().equalsIgnoreCase(staffId)) {
            throw new BusinessLogicException("Staff không có quyền thao tác claim này.");
        }

        if (claim.getStatus() != WarrantyClaimStatus.HANDOVER) {
            throw new BusinessLogicException("Staff chỉ có thể hoàn tất sau khi technician đã bàn giao (HANDOVER).");
        }

        claim.setScStaffDone(done);
        if (done) claim.setStatus(WarrantyClaimStatus.DONE);

        warrantyClaimRepository.save(claim);
        return mapToResponse(claim);
    }

    /**
     * EVM thêm mô tả trong khi claim đang REPAIR
     */
    @Transactional
    public WarrantyClaimResponse updateEvmDescription(String claimId, String evmId, String description) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found: " + claimId));

        if (claim.getEvm() == null ||
                !claim.getEvm().getAccountId().equalsIgnoreCase(evmId)) {
            throw new BusinessLogicException("EVM không được phép chỉnh claim này.");
        }

        // YÊU CẦU: chỉ được thêm mô tả khi claim đang DECIDE
        if (claim.getEvm() == null) {
            throw new BusinessLogicException("Claim chưa được gán EVM.");
        }
        if (claim.getStatus() != WarrantyClaimStatus.DECIDE) {
            throw new BusinessLogicException("Chỉ có thể thêm mô tả khi claim ở DECIDE.");
        }


        claim.setEvmDescription(description);
        // Khi EVM điền description => chuyển DECIDE -> REPAIR
        claim.setStatus(WarrantyClaimStatus.REPAIR);

        warrantyClaimRepository.save(claim);

        repairPartService.handleRepairParts(claimId);

        return mapToResponse(claim);
    }


    @Transactional
    public WarrantyClaimResponse technicianSkipRepair(String claimId, String technicianId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found: " + claimId));

        // Xác minh technician có quyền
        if (claim.getServiceCenterTechnician() == null ||
                !claim.getServiceCenterTechnician().getAccountId().equalsIgnoreCase(technicianId)) {
            throw new BusinessLogicException("Technician không có quyền thao tác claim này.");
        }

        // Chỉ cho phép khi đang CHECK
        if (claim.getStatus() != WarrantyClaimStatus.CHECK) {
            throw new BusinessLogicException("Chỉ có thể bỏ qua sửa chữa khi claim đang ở trạng thái CHECK.");
        }

        // Đặt lại trạng thái
        claim.setIsRepair(false);
        claim.setStatus(WarrantyClaimStatus.HANDOVER);
        claim.setTechnicianDone(true); // Đánh dấu đã xử lý xong
        warrantyClaimRepository.save(claim);

        return mapToResponse(claim);
    }

    private WarrantyClaimResponse mapToResponse(WarrantyClaim claim) {
        WarrantyClaimResponse res = modelMapper.map(claim, WarrantyClaimResponse.class);
        res.setStatus(claim.getStatus().name());
        return res;
    }

    /**
     * SC Staff bỏ qua bước EVM, chuyển DECIDE → HANDOVER khi claim chưa có EVM.
     */
    @Transactional
    public WarrantyClaimResponse scStaffSkipEvmAndHandover(String claimId, String staffId) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Claim: " + claimId));

        // Kiểm tra quyền của Staff
        if (claim.getServiceCenterStaff() == null ||
                !claim.getServiceCenterStaff().getAccountId().equalsIgnoreCase(staffId)) {
            throw new BusinessLogicException("Staff không có quyền thao tác claim này.");
        }

        // Phải ở trạng thái DECIDE
        if (claim.getStatus() != WarrantyClaimStatus.DECIDE) {
            throw new BusinessLogicException("Chỉ có thể bỏ qua EVM khi claim đang ở trạng thái DECIDE.");
        }

        // Chỉ khi chưa có EVM
        if (claim.getEvm() != null) {
            throw new BusinessLogicException("Claim đã có EVM, không thể bỏ qua bước DECIDE.");
        }

        // Chuyển sang HANDOVER
        claim.setStatus(WarrantyClaimStatus.HANDOVER);
        warrantyClaimRepository.save(claim);

        return mapToResponse(claim);
    }


}
