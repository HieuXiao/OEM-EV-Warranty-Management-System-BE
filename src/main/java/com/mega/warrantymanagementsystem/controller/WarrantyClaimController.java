package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.model.request.ClaimAttachmentRequest;
import com.mega.warrantymanagementsystem.model.request.ClaimReplacementPartRequest;
import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.service.WarrantyClaimService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/warranty_claims")
@CrossOrigin
@SecurityRequirement(name = "api")
public class WarrantyClaimController {

    @Autowired
    private WarrantyClaimService warrantyClaimService;

    @PostMapping("/create")
    public ResponseEntity<WarrantyClaimResponse> createWarrantyClaim(@Valid @RequestBody WarrantyClaimRequest request) {
        WarrantyClaimResponse response = warrantyClaimService.createWarrantyClaim(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<WarrantyClaimResponse> updateWarrantyClaim(
            @PathVariable("id") int claimId,
            @Valid @RequestBody WarrantyClaimRequest request) {
        WarrantyClaimResponse response = warrantyClaimService.updateWarrantyClaim(claimId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarrantyClaimResponse> getWarrantyClaimById(@PathVariable("id") int claimId) {
        WarrantyClaimResponse response = warrantyClaimService.findById(claimId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<WarrantyClaimResponse>> getAllWarrantyClaims() {
        return ResponseEntity.ok(warrantyClaimService.findAll());
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<WarrantyClaimResponse>> getByStaff(@PathVariable("staffId") String staffId) {
        return ResponseEntity.ok(warrantyClaimService.findByStaffId(staffId));
    }

    @GetMapping("/technician/{techId}")
    public ResponseEntity<List<WarrantyClaimResponse>> getByTechnician(@PathVariable("techId") String techId) {
        return ResponseEntity.ok(warrantyClaimService.findByTechnicianId(techId));
    }

    @GetMapping("/evm/{evmId}")
    public ResponseEntity<List<WarrantyClaimResponse>> getByEvm(@PathVariable("evmId") String evmId) {
        return ResponseEntity.ok(warrantyClaimService.findByEvmId(evmId));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<WarrantyClaimResponse>> getByPolicy(@PathVariable("policyId") int policyId) {
        return ResponseEntity.ok(warrantyClaimService.findByPolicyId(policyId));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<WarrantyClaimResponse>> getByDate(@PathVariable("date") LocalDate date) {
        return ResponseEntity.ok(warrantyClaimService.findByClaimDate(date));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<WarrantyClaimResponse>> getByStatus(@PathVariable("status") WarrantyClaimStatus status) {
        return ResponseEntity.ok(warrantyClaimService.findByStatus(status));
    }

    @GetMapping("/vin/{vin}")
    public ResponseEntity<List<WarrantyClaimResponse>> getByVin(@PathVariable("vin") String vin) {
        return ResponseEntity.ok(warrantyClaimService.findByVehicleVin(vin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarrantyClaim(@PathVariable("id") int claimId) {
        warrantyClaimService.deleteWarrantyClaim(claimId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{claimId}/status")
    public ResponseEntity<WarrantyClaimResponse> updateStatus(
            @PathVariable int claimId,
            @RequestParam WarrantyClaimStatus status) {

        WarrantyClaimResponse response = warrantyClaimService.updateStatus(claimId, status);
        return ResponseEntity.ok(response);
    }

    // ---------------- ADD ATTACHMENT ----------------
    @PostMapping("/{claimId}/attachments/{attachmentId}")
    public ResponseEntity<Void> addAttachmentToClaim(
            @PathVariable int claimId,
            @PathVariable int attachmentId) {
        warrantyClaimService.addAttachmentToClaim(claimId, attachmentId);
        return ResponseEntity.ok().build();
    }

    // ---------------- DELETE ATTACHMENT ----------------
    @DeleteMapping("/{claimId}/attachments/{attachmentId}")
    public ResponseEntity<Void> removeAttachmentFromClaim(
            @PathVariable int claimId,
            @PathVariable int attachmentId) {
        warrantyClaimService.removeAttachmentFromClaim(claimId, attachmentId);
        return ResponseEntity.noContent().build();
    }


    // ---------------- ADD REPLACEMENT PART ----------------
    @PostMapping("/{claimId}/replacement-parts")
    public ResponseEntity<Void> addReplacementPartToClaim(
            @PathVariable int claimId,
            @Valid @RequestBody ClaimReplacementPartRequest request) {
        warrantyClaimService.addReplacementPartToClaim(claimId, request);
        return ResponseEntity.ok().build();
    }

    // Gắn record vào claim
    @PostMapping("/{claimId}/service_records/{recordId}")
    public ResponseEntity<Void> addServiceRecordToClaim(
            @PathVariable int claimId,
            @PathVariable int recordId) {
        warrantyClaimService.addServiceRecordToClaim(claimId, recordId);
        return ResponseEntity.ok().build();
    }

    // Tách record khỏi claim
    @DeleteMapping("/{claimId}/service_records/{recordId}")
    public ResponseEntity<Void> removeServiceRecordFromClaim(
            @PathVariable int claimId,
            @PathVariable int recordId) {
        warrantyClaimService.removeServiceRecordFromClaim(claimId, recordId);
        return ResponseEntity.noContent().build();
    }

}
