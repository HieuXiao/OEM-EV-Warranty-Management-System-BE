package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.WarehouseRequest;
import com.mega.warrantymanagementsystem.model.response.WarehouseResponse;
import com.mega.warrantymanagementsystem.service.WarehouseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller quản lý Warehouse.
 * Cung cấp các API CRUD và search theo name, location, id, all.
 */
@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin
@SecurityRequirement(name = "api")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    /**
     * Tạo mới Warehouse.
     */
    @PostMapping
    public WarehouseResponse create(@RequestBody WarehouseRequest request) {
        return warehouseService.create(request);
    }

    /**
     * Cập nhật Warehouse theo ID.
     */
    @PutMapping("/{id}")
    public WarehouseResponse update(@PathVariable Integer id,
                                    @RequestBody WarehouseRequest request) {
        return warehouseService.update(id, request);
    }

    /**
     * Xóa Warehouse theo ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        warehouseService.delete(id);
    }

    /**
     * Lấy tất cả Warehouse.
     */
    @GetMapping
    public List<WarehouseResponse> getAll() {
        return warehouseService.getAll();
    }

    /**
     * Lấy Warehouse theo ID.
     */
    @GetMapping("/{id}")
    public WarehouseResponse getById(@PathVariable Integer id) {
        return warehouseService.getById(id);
    }

    /**
     * Tìm kiếm theo tên kho.
     * /api/warehouses/search/name?value=Main
     */
    @GetMapping("/search/name")
    public List<WarehouseResponse> getByName(@RequestParam("value") String name) {
        return warehouseService.getByName(name);
    }

    /**
     * Tìm kiếm theo vị trí kho.
     * /api/warehouses/search/location?value=HCM
     */
    @GetMapping("/search/location")
    public List<WarehouseResponse> getByLocation(@RequestParam("value") String location) {
        return warehouseService.getByLocation(location);
    }
}
