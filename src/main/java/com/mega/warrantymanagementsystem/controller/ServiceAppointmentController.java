package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.ServiceAppointment;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ServiceAppointmentRequest;
import com.mega.warrantymanagementsystem.service.ServiceAppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // biểu thị đây là controller
@RequestMapping("/api/service-appointment") // đường dẫn chung
@CrossOrigin // cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")
public class ServiceAppointmentController {

    @Autowired
    private ServiceAppointmentService serviceAppointmentService;

    @PostMapping("/create")
    public ResponseEntity<ServiceAppointment> createAppointment(
            @RequestBody ServiceAppointmentRequest request) {
        ServiceAppointment created = serviceAppointmentService.createAppointment(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/")
    public ResponseEntity<List<ServiceAppointment>> getAllAppointments() {
        List<ServiceAppointment> appointments = serviceAppointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/vin")
    public ResponseEntity<List<ServiceAppointment>> getAppointmentsByVin(@RequestParam("vin") String vin) {
        List<ServiceAppointment> appointments = serviceAppointmentService.getAppointmentsByVin(vin);
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found for VIN: " + vin);
        }
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/campaign")
    public ResponseEntity<List<ServiceAppointment>> getAppointmentsByCampaign(@RequestParam("campaignId") int campaignId) {
        List<ServiceAppointment> appointments = serviceAppointmentService.getAppointmentsByCampaign(campaignId);
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found for campaign ID: " + campaignId);
        }
        return ResponseEntity.ok(appointments);
    }
}
