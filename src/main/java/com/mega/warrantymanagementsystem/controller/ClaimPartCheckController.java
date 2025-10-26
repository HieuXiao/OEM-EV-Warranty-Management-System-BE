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
@CrossOrigin//cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")
public class ClaimPartCheckController {

    @Autowired
    private ClaimPartCheckService claimPartCheckService;

    // ================= CREATE =================
    @PostMapping("/create")
    public ResponseEntity<ClaimPartCheckResponse> create(@RequestBody ClaimPartCheckRequest request) {
        ClaimPartCheckResponse response = claimPartCheckService.create(request);
        return ResponseEntity.ok(response);
    }

    // ================= UPDATE =================
    @PutMapping("/update/{partNumber}")
    public ResponseEntity<ClaimPartCheckResponse> update(@PathVariable String partNumber,
                                                         @RequestBody ClaimPartCheckRequest request) {
        ClaimPartCheckResponse response = claimPartCheckService.update(partNumber, request);
        return ResponseEntity.ok(response);
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{partNumber}")
    public ResponseEntity<String> delete(@PathVariable String partNumber) {
        claimPartCheckService.delete(partNumber);
        return ResponseEntity.ok("ClaimPartCheck với PartNumber " + partNumber + " đã được xóa thành công");
    }

    // ================= GET ALL =================
    @GetMapping("/all")
    public ResponseEntity<List<ClaimPartCheckResponse>> getAll() {
        List<ClaimPartCheckResponse> list = claimPartCheckService.getAll();
        return ResponseEntity.ok(list);
    }

    // ================= GET BY PART NUMBER =================
    @GetMapping("/get/{partNumber}")
    public ResponseEntity<ClaimPartCheckResponse> getByPartNumber(@PathVariable String partNumber) {
        ClaimPartCheckResponse response = claimPartCheckService.getByPartNumber(partNumber);
        return ResponseEntity.ok(response);
    }

    // ================= SEARCH BY VIN =================
    @GetMapping("/search/vin/{vin}")
    public ResponseEntity<List<ClaimPartCheckResponse>> searchByVin(@PathVariable String vin) {
        List<ClaimPartCheckResponse> list = claimPartCheckService.getByVin(vin);
        return ResponseEntity.ok(list);
    }

    // ================= SEARCH BY WARRANTY ID =================
    @GetMapping("/search/warranty/{warrantyId}")
    public ResponseEntity<List<ClaimPartCheckResponse>> searchByWarrantyId(@PathVariable String warrantyId) {
        List<ClaimPartCheckResponse> list = claimPartCheckService.getByWarrantyId(warrantyId);
        return ResponseEntity.ok(list);
    }
}
