package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.PolicyRequest;
import com.mega.warrantymanagementsystem.model.response.PolicyResponse;
import com.mega.warrantymanagementsystem.service.PolicyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller chính cho Policy — chứa CRUD và các API search nhanh.
 */
@RestController
@RequestMapping("/api/policies")
@CrossOrigin
@SecurityRequirement(name = "api")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    // ---------------- CREATE ----------------
    @PostMapping
    public PolicyResponse create(@RequestBody PolicyRequest request) {
        return policyService.create(request);
    }

    // ---------------- READ ----------------
    @GetMapping
    public List<PolicyResponse> getAll() {
        return policyService.getAll();
    }

    @GetMapping("/{id}")
    public PolicyResponse getById(@PathVariable Integer id) {
        return policyService.getById(id);
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public PolicyResponse update(@PathVariable Integer id, @RequestBody PolicyRequest request) {
        return policyService.update(id, request);
    }

    // ---------------- DELETE ----------------
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        policyService.delete(id);
    }

    // ---------------- SEARCH QUICK ----------------
    @GetMapping("/getByName")
    public List<PolicyResponse> getByName(@RequestParam String name) {
        return policyService.getByName(name);
    }

    @GetMapping("/getByEnable")
    public List<PolicyResponse> getByEnable(@RequestParam Boolean enable) {
        return policyService.getByEnable(enable);
    }

    // ---------------- UPDATE ENABLE ----------------
    @PatchMapping("/{id}/enable")
    public PolicyResponse updateEnable(@PathVariable Integer id, @RequestParam Boolean isEnable) {
        return policyService.updateEnable(id, isEnable);
    }
}
