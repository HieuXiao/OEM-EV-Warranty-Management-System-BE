package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.ClaimPartCheckRequest;
import com.mega.warrantymanagementsystem.model.response.ClaimPartCheckResponse;
import com.mega.warrantymanagementsystem.service.ClaimPartCheckService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claim-part-check")
@CrossOrigin
@SecurityRequirement(name = "api")
public class ClaimPartCheckController {

    @Autowired
    private ClaimPartCheckService claimPartCheckService;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<ClaimPartCheckResponse> create(@RequestBody ClaimPartCheckRequest request) {
        ClaimPartCheckResponse response = claimPartCheckService.create(request);
        return ResponseEntity.ok(response);
    }

    // UPDATE
    @PutMapping("/update/{claimId}/{partNumber}")
    public ResponseEntity<ClaimPartCheckResponse> update(@PathVariable String claimId,
                                                         @PathVariable String partNumber,
                                                         @RequestBody ClaimPartCheckRequest request) {
        ClaimPartCheckResponse response = claimPartCheckService.update(claimId, partNumber, request);
        return ResponseEntity.ok(response);
    }

    // ADD SERIAL
    @PostMapping("/add-serials/{claimId}/{partNumber}")
    public ResponseEntity<ClaimPartCheckResponse> addPartSerials(
            @PathVariable String claimId,
            @PathVariable String partNumber,
            @RequestBody List<String> serialCodes) {
        ClaimPartCheckResponse response = claimPartCheckService.addPartSerials(claimId, partNumber, serialCodes);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/delete/{claimId}/{partNumber}")
    public ResponseEntity<String> delete(@PathVariable String claimId, @PathVariable String partNumber) {
        claimPartCheckService.delete(claimId, partNumber);
        return ResponseEntity.ok("Đã xóa part " + partNumber + " trong claim " + claimId + " thành công");
    }

    // GET ALL
    @GetMapping("/all")
    public ResponseEntity<List<ClaimPartCheckResponse>> getAll() {
        return ResponseEntity.ok(claimPartCheckService.getAll());
    }

    // GET BY CLAIM + PART
    @GetMapping("/get/{claimId}/{partNumber}")
    public ResponseEntity<ClaimPartCheckResponse> getByPartNumber(
            @PathVariable String claimId,
            @PathVariable String partNumber) {
        ClaimPartCheckResponse response = claimPartCheckService.getByPartNumber(claimId, partNumber);
        return ResponseEntity.ok(response);
    }

    // SEARCH BY VIN
    @GetMapping("/search/vin/{vin}")
    public ResponseEntity<List<ClaimPartCheckResponse>> searchByVin(@PathVariable String vin) {
        return ResponseEntity.ok(claimPartCheckService.getByVin(vin));
    }

    // SEARCH BY WARRANTY ID
    @GetMapping("/search/warranty/{warrantyId}")
    public ResponseEntity<List<ClaimPartCheckResponse>> searchByWarrantyId(@PathVariable String warrantyId) {
        return ResponseEntity.ok(claimPartCheckService.getByWarrantyId(warrantyId));
    }

    // DELETE ALL CLAIM PART CHECK BY WARRNTY ID
    @DeleteMapping("/delete-all/{claimId}")
    public ResponseEntity<String> deleteAll(@PathVariable String claimId) {
        claimPartCheckService.deleteAllByClaim(claimId);
        return ResponseEntity.ok("Đã xóa toàn bộ ClaimPartCheck thuộc claim " + claimId);
    }
}
