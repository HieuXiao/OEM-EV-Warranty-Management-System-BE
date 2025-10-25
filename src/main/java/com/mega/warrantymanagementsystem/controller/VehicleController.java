//package com.mega.warrantymanagementsystem.controller;
//
//import com.mega.warrantymanagementsystem.entity.Vehicle;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.VehicleRequest;
//import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
//import com.mega.warrantymanagementsystem.service.VehicleService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController//biểu thị đây là controller
//@RequestMapping("/api/vehicle")//đường dẫn chung
//@CrossOrigin//cho phép mọi nguồn truy cập
//@SecurityRequirement(name = "api")
//public class VehicleController {
//
//    @Autowired
//    VehicleService vehicleService;
//
//    @PostMapping("/create")
//    public ResponseEntity<VehicleResponse> createVehicle(@RequestBody @Valid VehicleRequest vehicleRequest) {
//        VehicleResponse created = vehicleService.createVehicle(vehicleRequest);
//        return ResponseEntity.ok(created);
//    }
//
//    @GetMapping("/search/by-model/{model}")
//    public ResponseEntity<List<VehicleResponse>> getVehiclesByModel(@PathVariable String model) {
//        List<VehicleResponse> vehicles = vehicleService.findByModel(model);
//        return ResponseEntity.ok(vehicles);
//    }
//
//    @GetMapping("/{vin}")
//    public ResponseEntity<VehicleResponse> getByVin(@PathVariable String vin){
//        VehicleResponse vehicle = vehicleService.findByVin(vin);
//        if(vehicle == null){
//            throw new ResourceNotFoundException("Vehicle not found with VIN: " + vin);
//        }
//        return ResponseEntity.ok(vehicle);
//    }
//
//    @GetMapping("/customer/{customerPhone}")
//    public ResponseEntity<List<VehicleResponse>> getVehicleByCustomer(@PathVariable String customerPhone) {
//        List<VehicleResponse> vehicles = vehicleService.findByCustomerPhone(customerPhone);
//        if (vehicles.isEmpty()) {
//            throw new ResourceNotFoundException("No vehicles found for customerPhone: " + customerPhone);
//        }
//        return ResponseEntity.ok(vehicles);
//    }
//
//    @GetMapping("/")
//    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
//        return ResponseEntity.ok(vehicleService.getAllVehicles());
//    }
//
//    @PutMapping("/assign-campaign/{campaignId}/model/{model}")
//    public ResponseEntity<String> assignCampaignToVehiclesByModel(
//            @PathVariable int campaignId,
//            @PathVariable String model) {
//        vehicleService.assignCampaignToVehiclesByModel(campaignId, model);
//        return ResponseEntity.ok("Campaign " + campaignId + " assigned to all vehicles with model: " + model);
//    }
//
//    @PutMapping("/remove-campaign/{model}")
//    public ResponseEntity<String> removeCampaignFromModel(@PathVariable String model) {
//        vehicleService.removeCampaignFromModel(model);
//        return ResponseEntity.ok("Removed campaign from all vehicles of model: " + model);
//    }
//
//    @PutMapping("/assign-campaign/{campaignId}/vin/{vin}")
//    public ResponseEntity<String> assignCampaignToVehicleByVin(
//            @PathVariable int campaignId,
//            @PathVariable String vin) {
//        vehicleService.assignCampaignToVehicleByVin(campaignId, vin);
//        return ResponseEntity.ok("Campaign " + campaignId + " assigned to vehicle VIN: " + vin);
//    }
//
//    @PutMapping("/remove-campaign/vin/{vin}")
//    public ResponseEntity<String> removeCampaignFromVehicleByVin(@PathVariable String vin) {
//        vehicleService.removeCampaignFromVehicleByVin(vin);
//        return ResponseEntity.ok("Removed campaign from vehicle VIN: " + vin);
//    }
//
//
//}
