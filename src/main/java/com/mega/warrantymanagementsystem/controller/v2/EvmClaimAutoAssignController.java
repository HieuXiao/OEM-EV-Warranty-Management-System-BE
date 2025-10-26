package com.mega.warrantymanagementsystem.controller.v2;

import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.service.v2.EvmClaimAutoAssignService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warranty-claims/assign-evm")
@CrossOrigin
@SecurityRequirement(name = "api")
public class EvmClaimAutoAssignController {

    @Autowired
    private EvmClaimAutoAssignService evmClaimAutoAssignService;

    /**
     * Gán tất cả claim ở trạng thái DECIDE cho EVM staff (theo thứ tự vòng lặp)
     */
    @PostMapping("/auto")
    public ResponseEntity<List<WarrantyClaimResponse>> autoAssignDecideClaimsToEvm() {
        List<WarrantyClaimResponse> responses = evmClaimAutoAssignService.autoAssignDecideClaimsToEvm();
        return ResponseEntity.ok(responses);
    }
}
