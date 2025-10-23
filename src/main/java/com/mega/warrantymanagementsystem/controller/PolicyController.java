package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.PolicyRequest;
import com.mega.warrantymanagementsystem.model.response.PolicyResponse;
import com.mega.warrantymanagementsystem.service.PolicyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin
@SecurityRequirement(name = "api")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    //------------------Get All Policies------------------------
    @GetMapping("/")
    public ResponseEntity<List<PolicyResponse>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    //------------------Get Policy By Id------------------------
    @GetMapping("/{policyId}getByID")
    public ResponseEntity<PolicyResponse> getPolicyById(@PathVariable("policyId") int policyId) {
        PolicyResponse policy = policyService.getPolicyById(policyId);
        if (policy == null) {
            throw new ResourceNotFoundException("Policy not found with ID: " + policyId);
        }
        return ResponseEntity.ok(policy);
    }

    //------------------Create Policy------------------------
    @PostMapping("/create")
    public ResponseEntity<PolicyResponse> createPolicy(@RequestBody PolicyRequest policyRequest) {
        PolicyResponse created = policyService.createPolicy(policyRequest);
        return ResponseEntity.ok(created);
    }

    //------------------Update Policy------------------------
    @PutMapping("/{policyId}update")
    public ResponseEntity<PolicyResponse> updatePolicy(
            @PathVariable("policyId") int policyId,
            @RequestBody PolicyRequest policyRequest) {

        PolicyResponse updated = policyService.updatePolicy(policyId, policyRequest);
        return ResponseEntity.ok(updated);
    }

    //------------------Delete Policy------------------------
    @DeleteMapping("/{policyId}delete")
    public ResponseEntity<Void> deletePolicy(@PathVariable("policyId") int policyId) {
        policyService.deletePolicy(policyId);
        return ResponseEntity.noContent().build();
    }
}
