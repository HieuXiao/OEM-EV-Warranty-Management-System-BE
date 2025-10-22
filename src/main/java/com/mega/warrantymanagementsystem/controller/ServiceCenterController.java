package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.ServiceCenter;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ServiceCenterRequest;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.service.ServiceCenterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//biểu thị đây là controller
@RequestMapping("/api/service_centers")//đường dẫn chung
@CrossOrigin//cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")
public class ServiceCenterController {

    @Autowired
    ServiceCenterService serviceCenterService;

    @PostMapping("/create")
    public ResponseEntity<ServiceCenterResponse> createServiceCenter(
            @Valid @RequestBody ServiceCenterRequest serviceCenterRequest) {

        ServiceCenterResponse response = serviceCenterService.createServiceCenter(serviceCenterRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ServiceCenterResponse> getById(@PathVariable int id) {
        ServiceCenterResponse response = serviceCenterService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<ServiceCenterResponse>> getAllServiceCenter(){
        return ResponseEntity.ok(serviceCenterService.findAllServiceCenter());
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<ServiceCenterResponse>> getByLocation(@PathVariable String location) {
        List<ServiceCenterResponse> responses = serviceCenterService.findByLocation(location);
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/search/{name}")
    public ResponseEntity<ServiceCenterResponse> getServiceCenterByName(@PathVariable String name) {
        ServiceCenterResponse response = serviceCenterService.findByName(name);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ServiceCenterResponse> updateServiceCenter(
            @PathVariable int id,
            @RequestBody ServiceCenterRequest request) {
        ServiceCenterResponse updated = serviceCenterService.updateServiceCenter(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteServiceCenter(@PathVariable int id) {
        serviceCenterService.deleteServiceCenter(id);
        return ResponseEntity.ok("Deleted service center with id: " + id);
    }

}
