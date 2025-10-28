package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ServiceAppointmentRequest;
import com.mega.warrantymanagementsystem.model.response.ServiceAppointmentResponse;
import com.mega.warrantymanagementsystem.service.ServiceAppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller quản lý ServiceAppointment (cuộc hẹn dịch vụ).
 * Cung cấp các API CRUD và search theo ngày, mô tả, ID.
 */
@RestController
@RequestMapping("/api/service-appointments")
@CrossOrigin//cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")
public class ServiceAppointmentController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    /**
     * API tạo mới một cuộc hẹn dịch vụ.
     */
    @PostMapping
    public ServiceAppointmentResponse create(@RequestBody ServiceAppointmentRequest request) {
        return serviceAppointmentService.create(request);
    }

    /**
     * API cập nhật thông tin cuộc hẹn theo ID.
     */
    @PutMapping("/{id}")
    public ServiceAppointmentResponse update(@PathVariable Integer id,
                                             @RequestBody ServiceAppointmentRequest request) {
        return serviceAppointmentService.update(id, request);
    }

    /**
     * API xóa cuộc hẹn theo ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        serviceAppointmentService.delete(id);
    }

    /**
     * API lấy danh sách toàn bộ cuộc hẹn.
     */
    @GetMapping
    public List<ServiceAppointmentResponse> getAll() {
        return serviceAppointmentService.getAll();
    }

    /**
     * API lấy cuộc hẹn theo ID.
     */
    @GetMapping("/{id}")
    public ServiceAppointmentResponse getById(@PathVariable Integer id) {
        return serviceAppointmentService.getById(id);
    }

    /**
     * API tìm kiếm theo mô tả.
     * Ví dụ: /api/service-appointments/search/description?value=bảo+dưỡng
     */
    @GetMapping("/search/description")
    public List<ServiceAppointmentResponse> getByDescription(@RequestParam("value") String description) {
        return serviceAppointmentService.getByDescription(description);
    }

    /**
     * API tìm kiếm theo ngày (LocalDate).
     * Ví dụ: /api/service-appointments/search/date?value=2025-10-26
     */
    @GetMapping("/search/date")
    public List<ServiceAppointmentResponse> getByDate(
            @RequestParam("value") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date) {
        return serviceAppointmentService.getByDate(date);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable("id") int id,
            @RequestParam("status") String status) {

        try {
            String message = serviceAppointmentService.updateStatus(id, status);
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("System error: " + e.getMessage());
        }
    }
}
