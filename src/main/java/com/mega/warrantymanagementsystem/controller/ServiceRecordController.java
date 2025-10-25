//package com.mega.warrantymanagementsystem.controller;
//
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.ServiceRecordRequest;
//import com.mega.warrantymanagementsystem.model.response.ServiceRecordResponse;
//import com.mega.warrantymanagementsystem.service.ServiceRecordService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/service_records")
//@CrossOrigin
//@SecurityRequirement(name = "api")
//public class ServiceRecordController {
//
//    @Autowired
//    private ServiceRecordService serviceRecordService;
//
//    @PostMapping("/create")
//    public ResponseEntity<ServiceRecordResponse> create(@RequestBody @Valid ServiceRecordRequest request) {
//        ServiceRecordResponse response = serviceRecordService.createServiceRecord(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PutMapping("/{recordId}")
//    public ResponseEntity<ServiceRecordResponse> update(@PathVariable int recordId,
//                                                        @RequestBody @Valid ServiceRecordRequest request) {
//        ServiceRecordResponse response = serviceRecordService.updateServiceRecord(recordId, request);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{recordId}")
//    public ResponseEntity<ServiceRecordResponse> getById(@PathVariable int recordId) {
//        ServiceRecordResponse response = serviceRecordService.findById(recordId);
//        if (response == null) {
//            throw new ResourceNotFoundException("ServiceRecord not found with ID: " + recordId);
//        }
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/")
//    public ResponseEntity<List<ServiceRecordResponse>> getAll() {
//        return ResponseEntity.ok(serviceRecordService.findAllServiceRecords());
//    }
//
//    @DeleteMapping("/{recordId}")
//    public ResponseEntity<String> delete(@PathVariable int recordId) {
//        serviceRecordService.deleteServiceRecord(recordId);
//        return ResponseEntity.ok("Deleted ServiceRecord with ID: " + recordId);
//    }
//
//    // Các API tìm kiếm nâng cao
//    @GetMapping("/vehicle/{vin}")
//    public ResponseEntity<List<ServiceRecordResponse>> getByVehicle(@PathVariable String vin) {
//        return ResponseEntity.ok(serviceRecordService.findByVehicleVin(vin));
//    }
//
//    @GetMapping("/campaign/{campaignId}")
//    public ResponseEntity<List<ServiceRecordResponse>> getByCampaign(@PathVariable int campaignId) {
//        return ResponseEntity.ok(serviceRecordService.findByCampaignId(campaignId));
//    }
//
//    @GetMapping("/center/{centerId}")
//    public ResponseEntity<List<ServiceRecordResponse>> getByCenter(@PathVariable int centerId) {
//        return ResponseEntity.ok(serviceRecordService.findByServiceCenterId(centerId));
//    }
//
//    @GetMapping("/appointment/{appointmentId}")
//    public ResponseEntity<List<ServiceRecordResponse>> getByAppointment(@PathVariable int appointmentId) {
//        return ResponseEntity.ok(serviceRecordService.findByAppointmentId(appointmentId));
//    }
//
//    // Gắn campaign cho ServiceRecord
//    @PutMapping("/{recordId}/assign-campaign/{campaignId}")
//    public ResponseEntity<String> assignCampaignToRecord(
//            @PathVariable int recordId,
//            @PathVariable int campaignId) {
//
//        serviceRecordService.assignCampaignToRecord(recordId, campaignId);
//        return ResponseEntity.ok("Assigned campaign " + campaignId + " to ServiceRecord " + recordId);
//    }
//
//    // Gỡ campaign khỏi ServiceRecord
//    @PutMapping("/{recordId}/remove-campaign")
//    public ResponseEntity<String> removeCampaignFromRecord(@PathVariable int recordId) {
//        serviceRecordService.removeCampaignFromRecord(recordId);
//        return ResponseEntity.ok("Removed campaign from ServiceRecord " + recordId);
//    }
//
//
//}
