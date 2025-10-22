package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.service.WarrantyClaimService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warranty_claims/manage")
@CrossOrigin
@SecurityRequirement(name = "api")
public class WarrantyClaimManagerController {

    @Autowired
    private WarrantyClaimService warrantyClaimService;

    // ---------------- ATTACHMENTS ----------------
    @PostMapping("/{claimId}/attachments/{attachmentId}")
    public ResponseEntity<Void> addAttachmentToClaim(
            @PathVariable int claimId,
            @PathVariable int attachmentId) {
        warrantyClaimService.addAttachmentToClaim(claimId, attachmentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{claimId}/attachments/{attachmentId}")
    public ResponseEntity<Void> removeAttachmentFromClaim(
            @PathVariable int claimId,
            @PathVariable int attachmentId) {
        warrantyClaimService.removeAttachmentFromClaim(claimId, attachmentId);
        return ResponseEntity.noContent().build();
    }

    // ---------------- REPLACEMENT PARTS ----------------
    @PostMapping("/{claimId}/replacement-parts/{partUserId}")
    public ResponseEntity<Void> addReplacementPartToClaim(
            @PathVariable int claimId,
            @PathVariable int partUserId) {
        warrantyClaimService.addReplacementPartToClaim(claimId, partUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{claimId}/replacement-parts/{partUserId}")
    public ResponseEntity<Void> removeReplacementPartFromClaim(
            @PathVariable int claimId,
            @PathVariable int partUserId) {
        warrantyClaimService.removeReplacementPartFromClaim(claimId, partUserId);
        return ResponseEntity.noContent().build();
    }

    // ---------------- SERVICE RECORDS ----------------
    @PostMapping("/{claimId}/service_records/{recordId}")
    public ResponseEntity<Void> addServiceRecordToClaim(
            @PathVariable int claimId,
            @PathVariable int recordId) {
        warrantyClaimService.addServiceRecordToClaim(claimId, recordId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{claimId}/service_records/{recordId}")
    public ResponseEntity<Void> removeServiceRecordFromClaim(
            @PathVariable int claimId,
            @PathVariable int recordId) {
        warrantyClaimService.removeServiceRecordFromClaim(claimId, recordId);
        return ResponseEntity.noContent().build();
    }

    // --------------- CAMPAIGN --------------------
    @PostMapping("/{claimId}/campaigns/{campaignId}")
    public ResponseEntity<Void> addCampaignToClaim(
            @PathVariable int claimId,
            @PathVariable int campaignId) {
        warrantyClaimService.addCampaignToClaim(claimId, campaignId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{claimId}/campaigns/{campaignId}")
    public ResponseEntity<Void> removeCampaignFromClaim(
            @PathVariable int claimId,
            @PathVariable int campaignId) {
        warrantyClaimService.removeCampaignFromClaim(claimId, campaignId);
        return ResponseEntity.noContent().build();
    }


}
