package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.service.WarrantyClaimService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller cho WarrantyClaim
 * CRUD + Search cơ bản
 */
@RestController
@RequestMapping("/api/warranty-claims")
@CrossOrigin
@SecurityRequirement(name = "api")
public class WarrantyClaimController {

    @Autowired
    private WarrantyClaimService warrantyClaimService;

    // CREATE
    @PostMapping
    public WarrantyClaimResponse create(@RequestBody WarrantyClaimRequest request) {
        return warrantyClaimService.create(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public WarrantyClaimResponse update(@PathVariable String id, @RequestBody WarrantyClaimRequest request) {
        return warrantyClaimService.update(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        warrantyClaimService.delete(id);
    }

    // GET ALL
    @GetMapping
    public List<WarrantyClaimResponse> getAll() {
        return warrantyClaimService.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public WarrantyClaimResponse getById(@PathVariable String id) {
        return warrantyClaimService.getById(id);
    }

    // SEARCH BY DATE
    @GetMapping("/by-date")
    public List<WarrantyClaimResponse> getByClaimDate(@RequestParam LocalDate date) {
        return warrantyClaimService.getByClaimDate(date);
    }

    // SEARCH BY STATUS
    @GetMapping("/by-status")
    public List<WarrantyClaimResponse> getByStatus(@RequestParam String status) {
        return warrantyClaimService.getByStatus(status);
    }
}
