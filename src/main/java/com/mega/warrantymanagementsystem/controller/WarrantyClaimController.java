package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.WarrantyClaimRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.service.WarrantyClaimService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller cho WarrantyClaim
 * CRUD + Search cơ bản
 * - Trả ResponseEntity để quản lý status code rõ ràng.
 * - Giữ chuẩn RESTful và Swagger-friendly.
 */
@RestController
@RequestMapping("/api/warranty-claims")
@CrossOrigin
@SecurityRequirement(name = "api")
public class WarrantyClaimController {

    @Autowired
    private WarrantyClaimService warrantyClaimService;

    /**
     * Tạo mới WarrantyClaim
     * Status mặc định: CHECK
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<WarrantyClaimResponse> create(@RequestBody WarrantyClaimRequest request) {
        WarrantyClaimResponse response = warrantyClaimService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cập nhật thông tin claim (mô tả, ngày, vehicle, staff...)
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WarrantyClaimResponse> update(
            @PathVariable String id,
            @RequestBody WarrantyClaimRequest request) {

        WarrantyClaimResponse response = warrantyClaimService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Xóa claim theo ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        warrantyClaimService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Lấy tất cả WarrantyClaim
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<WarrantyClaimResponse>> getAll() {
        List<WarrantyClaimResponse> list = warrantyClaimService.getAll();
        return ResponseEntity.ok(list);
    }

    /**
     * Lấy claim theo ID
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<WarrantyClaimResponse> getById(@PathVariable String id) {
        WarrantyClaimResponse response = warrantyClaimService.getById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lọc claim theo ngày yêu cầu
     * @param date - format: yyyy-MM-dd
     */
    @GetMapping(value = "/by-date", produces = "application/json")
    public ResponseEntity<List<WarrantyClaimResponse>> getByClaimDate(@RequestParam LocalDate date) {
        List<WarrantyClaimResponse> list = warrantyClaimService.getByClaimDate(date);
        return ResponseEntity.ok(list);
    }

    /**
     * Lọc claim theo trạng thái (CHECK, DECIDE, REPAIR, DONE, ...)
     */
    @GetMapping(value = "/by-status", produces = "application/json")
    public ResponseEntity<List<WarrantyClaimResponse>> getByStatus(@RequestParam String status) {
        List<WarrantyClaimResponse> list = warrantyClaimService.getByStatus(status);
        return ResponseEntity.ok(list);
    }
}
