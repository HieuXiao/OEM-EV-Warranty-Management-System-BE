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
    public ResponseEntity<WarrantyClaimResponse> update(@PathVariable int id, @RequestBody WarrantyClaimRequest request) {
        return ResponseEntity.ok(warrantyClaimService.updateWarrantyClaim(id, request));
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

//    @GetMapping("/policy/{policyId}")
//    public ResponseEntity<List<WarrantyClaimResponse>> getByPolicy(@PathVariable("policyId") int policyId) {
//        return ResponseEntity.ok(warrantyClaimService.findByPolicyId(policyId));
//    }

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

    // Cập nhật mô tả từ EVM
    @PutMapping("/{claimId}/evm/description")
    public ResponseEntity<Void> updateEvmDescription(
            @PathVariable int claimId,
            @RequestParam("description") String description) {
        warrantyClaimService.updateEvmDescription(claimId, description);
        return ResponseEntity.ok().build();
    }

}
