package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.WarehouseRequest;
import com.mega.warrantymanagementsystem.model.response.WarehouseResponse;
import com.mega.warrantymanagementsystem.service.WarehouseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin
@SecurityRequirement(name = "api")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping
    public WarehouseResponse create(@RequestBody WarehouseRequest req) {
        return warehouseService.create(req);
    }

    @PutMapping("/{id}")
    public WarehouseResponse update(@PathVariable Integer id, @RequestBody WarehouseRequest req) {
        return warehouseService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        warehouseService.delete(id);
    }

    @GetMapping
    public List<WarehouseResponse> getAll() {
        return warehouseService.getAll();
    }

    @GetMapping("/{id}")
    public WarehouseResponse getById(@PathVariable Integer id) {
        return warehouseService.getById(id);
    }

    @GetMapping("/search/name")
    public List<WarehouseResponse> getByName(@RequestParam("value") String name) {
        return warehouseService.getByName(name);
    }

    @GetMapping("/search/location")
    public List<WarehouseResponse> getByLocation(@RequestParam("value") String location) {
        return warehouseService.getByLocation(location);
    }
}
