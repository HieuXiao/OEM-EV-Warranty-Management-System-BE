package com.mega.warrantymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega.warrantymanagementsystem.model.request.CampaignReportRequest;
import com.mega.warrantymanagementsystem.model.response.CampaignReportResponse;
import com.mega.warrantymanagementsystem.service.CampaignReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/campaign-reports")
@CrossOrigin
@SecurityRequirement(name = "api")
public class CampaignReportController {

    @Autowired
    private CampaignReportService campaignReportService;

    @Autowired
    private ObjectMapper objectMapper; // dùng để parse JSON từ multipart

    // ==================== CREATE ====================
    @Operation(summary = "Tạo CampaignReport (upload PDF + thông tin chi tiết)",
            description = "Upload nhiều file PDF cùng lúc, đồng thời nhập thông tin campaignId, serviceCenterId, submittedBy, ...")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CampaignReportResponse> createCampaignReport(
            @Parameter(description = "Thông tin báo cáo JSON", required = true)
            @RequestPart("request") String requestJson, // Swagger/Postman gửi JSON string
            @Parameter(description = "Các file PDF upload", required = true)
            @RequestPart("files") List<MultipartFile> files
    ) throws IOException {

        // Chuyển JSON string thành object
        CampaignReportRequest request = objectMapper.readValue(requestJson, CampaignReportRequest.class);

        return ResponseEntity.ok(campaignReportService.createReport(request, files));
    }

    // ==================== UPDATE ====================
    @PutMapping(value = "/update/{reportId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CampaignReportResponse> updateCampaignReport(
            @PathVariable int reportId,
            @RequestPart("request") String requestJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {

        CampaignReportRequest request = objectMapper.readValue(requestJson, CampaignReportRequest.class);
        return ResponseEntity.ok(campaignReportService.updateWithFiles(reportId, request, files));
    }

    // ==================== GET ALL ====================
    @GetMapping("/all")
    public ResponseEntity<List<CampaignReportResponse>> getAll() {
        return ResponseEntity.ok(campaignReportService.getAll());
    }

    // ==================== GET BY ID ====================
    @GetMapping("/{reportId}")
    public ResponseEntity<CampaignReportResponse> getById(@PathVariable int reportId) {
        return ResponseEntity.ok(campaignReportService.getById(reportId));
    }

    // ==================== SEARCH BY CAMPAIGN ====================
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<CampaignReportResponse>> getByCampaign(@PathVariable int campaignId) {
        return ResponseEntity.ok(campaignReportService.getByCampaignId(campaignId));
    }

    // ==================== SEARCH BY SERVICE CENTER ====================
    @GetMapping("/service-center/{centerId}")
    public ResponseEntity<List<CampaignReportResponse>> getByServiceCenter(@PathVariable int centerId) {
        return ResponseEntity.ok(campaignReportService.getByServiceCenterId(centerId));
    }

    // ==================== SEARCH BY ACCOUNT ====================
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<CampaignReportResponse>> getByAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(campaignReportService.getByAccountId(accountId));
    }

    // ==================== SEARCH BY DATE RANGE ====================
    @GetMapping("/date-range")
    public ResponseEntity<List<CampaignReportResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(campaignReportService.getByDateRange(start, end));
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> delete(@PathVariable int reportId) {
        campaignReportService.delete(reportId);
        return ResponseEntity.ok("Deleted CampaignReport ID: " + reportId);
    }

}
