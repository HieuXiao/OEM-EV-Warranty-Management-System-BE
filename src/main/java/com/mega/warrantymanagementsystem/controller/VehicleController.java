package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.VehicleRequest;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
import com.mega.warrantymanagementsystem.service.VehicleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller quản lý Vehicle.
 * Cung cấp các API CRUD và search theo VIN, plate, type, color, model.
 */
@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin
@SecurityRequirement(name = "api")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    /**
     * Tạo mới Vehicle.
     */
    @PostMapping
    public VehicleResponse create(@RequestBody VehicleRequest request) {
        return vehicleService.create(request);
    }

    /**
     * Cập nhật Vehicle theo VIN.
     */
    @PutMapping("/{vin}")
    public VehicleResponse update(@PathVariable String vin,
                                  @RequestBody VehicleRequest request) {
        return vehicleService.update(vin, request);
    }

    /**
     * Xóa Vehicle theo VIN.
     */
    @DeleteMapping("/{vin}")
    public void delete(@PathVariable String vin) {
        vehicleService.delete(vin);
    }

    /**
     * Lấy tất cả Vehicle.
     */
    @GetMapping
    public List<VehicleResponse> getAll() {
        return vehicleService.getAll();
    }

    /**
     * Lấy Vehicle theo VIN.
     */
    @GetMapping("/{vin}")
    public VehicleResponse getByVin(@PathVariable String vin) {
        return vehicleService.getByVin(vin);
    }

    /**
     * Tìm theo biển số (plate).
     */
    @GetMapping("/search/plate")
    public List<VehicleResponse> getByPlate(@RequestParam("value") String plate) {
        return vehicleService.getByPlate(plate);
    }

    /**
     * Tìm theo loại xe (type).
     */
    @GetMapping("/search/type")
    public List<VehicleResponse> getByType(@RequestParam("value") String type) {
        return vehicleService.getByType(type);
    }

    /**
     * Tìm theo màu xe (color).
     */
    @GetMapping("/search/color")
    public List<VehicleResponse> getByColor(@RequestParam("value") String color) {
        return vehicleService.getByColor(color);
    }

    /**
     * Tìm theo model xe.
     */
    @GetMapping("/search/model")
    public List<VehicleResponse> getByModel(@RequestParam("value") String model) {
        return vehicleService.getByModel(model);
    }
}
