//package com.mega.warrantymanagementsystem.controller;
//
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.ClaimAttachmentRequest;
//import com.mega.warrantymanagementsystem.model.response.ClaimAttachmentResponse;
//import com.mega.warrantymanagementsystem.service.ClaimAttachmentService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/claimAttachment")
//@CrossOrigin
//@SecurityRequirement(name = "api")
//public class ClaimAttachmentController {
//
//    @Autowired
//    private ClaimAttachmentService claimAttachmentService;
//
//    // CREATE
//    @PostMapping("/create")
//    public ResponseEntity<ClaimAttachmentResponse> createClaimAttachment(
//            @RequestBody @Valid ClaimAttachmentRequest request) {
//        return ResponseEntity.ok(claimAttachmentService.createClaimAttachment(request));
//    }
//
//    // UPDATE
//    @PutMapping("/{id}")
//    public ResponseEntity<ClaimAttachmentResponse> updateClaimAttachment(
//            @PathVariable int id,
//            @RequestBody @Valid ClaimAttachmentRequest request) {
//        return ResponseEntity.ok(claimAttachmentService.updateClaimAttachment(id, request));
//    }
//
//    // DELETE
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteClaimAttachment(@PathVariable int id) {
//        claimAttachmentService.deleteClaimAttachment(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // GET ALL
//    @GetMapping("/")
//    public ResponseEntity<List<ClaimAttachmentResponse>> getAllClaimAttachments() {
//        return ResponseEntity.ok(claimAttachmentService.findAllAttachment());
//    }
//
//    // GET BY ID
//    @GetMapping("/{id}")
//    public ResponseEntity<ClaimAttachmentResponse> getById(@PathVariable int id) {
//        ClaimAttachmentResponse response = claimAttachmentService.findByAttachmentId(id);
//        if (response == null) {
//            throw new ResourceNotFoundException("WarrantyFile not found with id " + id);
//        }
//        return ResponseEntity.ok(response);
//    }
//}
