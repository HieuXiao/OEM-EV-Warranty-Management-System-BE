//package com.mega.warrantymanagementsystem.controller;
//
//import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
//import com.mega.warrantymanagementsystem.service.EvmAutoAssignService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/warranty_claims/manage")
//@CrossOrigin
//@SecurityRequirement(name = "api")
//public class EvmAutoAssignController {
//
//    @Autowired
//    private EvmAutoAssignService evmAutoAssignService;
//
//    /**
//     * API tự động gán EVM Staff cho claim theo thứ tự tuần tự
//     */
//    @PostMapping("/{claimId}/assign-evm/auto")
//    public ResponseEntity<WarrantyClaimResponse> autoAssignEvm(
//            @PathVariable int claimId) {
//        WarrantyClaimResponse response = evmAutoAssignService.autoAssignEvmToClaim(claimId);
//        return ResponseEntity.ok(response);
//    }
//}
