package com.mega.warrantymanagementsystem.controller.v2;

import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.service.v2.ClaimWorkflowService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Quản lý luồng chuyển trạng thái và cập nhật mô tả của EVM.
 * - Technician → HANDOVER
 * - Staff → DONE
 * - EVM thêm mô tả
 */
@RestController
@RequestMapping("/api/warranty-claims/workflow")
@CrossOrigin
@SecurityRequirement(name = "api")
public class ClaimWorkflowController {

    @Autowired
    private ClaimWorkflowService claimWorkflowService;

    /**
     * Technician hoàn tất sửa chữa → chuyển từ REPAIR → HANDOVER
     */
    @PostMapping("/{claimId}/technician/done")
    public ResponseEntity<WarrantyClaimResponse> technicianDone(
            @PathVariable String claimId,
            @RequestParam String technicianId,
            @RequestParam boolean done) {
        return ResponseEntity.ok(claimWorkflowService.technicianToggleDone(claimId, technicianId, done));
    }

    /**
     * Staff xác nhận bàn giao → chuyển từ HANDOVER → DONE
     */
    @PostMapping("/{claimId}/staff/done")
    public ResponseEntity<WarrantyClaimResponse> staffDone(
            @PathVariable String claimId,
            @RequestParam String staffId,
            @RequestParam boolean done) {
        return ResponseEntity.ok(claimWorkflowService.scStaffToggleDone(claimId, staffId, done));
    }

    /**
     * EVM thêm mô tả (chỉ khi claim còn ở REPAIR)
     */
    @PostMapping("/{claimId}/evm/description")
    public ResponseEntity<WarrantyClaimResponse> updateEvmDescription(
            @PathVariable String claimId,
            @RequestParam String evmId,
            @RequestParam String description) {
        return ResponseEntity.ok(claimWorkflowService.updateEvmDescription(claimId, evmId, description));
    }

    /**
     * Technician bỏ qua sửa chữa → CHECK → HANDOVER (isRepair=false)
     */
    @PostMapping("/{claimId}/technician/skip-repair")
    public ResponseEntity<WarrantyClaimResponse> skipRepair(
            @PathVariable String claimId,
            @RequestParam String technicianId) {
        return ResponseEntity.ok(claimWorkflowService.technicianSkipRepair(claimId, technicianId));
    }

    /**
     * SC Staff bỏ qua bước EVM, chuyển DECIDE → HANDOVER nếu chưa có EVM.
     */
    @PostMapping("/{claimId}/staff/skip-evm")
    public ResponseEntity<WarrantyClaimResponse> skipEvmAndHandover(
            @PathVariable String claimId,
            @RequestParam String staffId) {
        return ResponseEntity.ok(claimWorkflowService.scStaffSkipEvmAndHandover(claimId, staffId));
    }

    /**
     * EVM staff quyết định chuyển trực tiếp từ DECIDE → HANDOVER (có thể kèm mô tả).
     */
    @PostMapping("/{claimId}/evm/decision-handover")
    public ResponseEntity<WarrantyClaimResponse> evmDecisionToHandover(
            @PathVariable String claimId,
            @RequestParam String evmId,
            @RequestParam(required = false) String description) {
        return ResponseEntity.ok(claimWorkflowService.evmDecisionToHandover(claimId, evmId, description));
    }



}
