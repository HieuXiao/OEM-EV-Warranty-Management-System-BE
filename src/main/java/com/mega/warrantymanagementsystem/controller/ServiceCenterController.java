package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.ServiceCenterRequest;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.service.ServiceCenterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller cho Service Center.
 * Cung cấp CRUD + search theo name, location, id, all.
 */
@RestController
@RequestMapping("/api/service-centers")
@CrossOrigin
@SecurityRequirement(name = "api")
public class ServiceCenterController {

    @Autowired
    private ServiceCenterService serviceCenterService;

    /**
     * Tạo mới Service Center.
     */
    @PostMapping
    public ServiceCenterResponse create(@RequestBody ServiceCenterRequest request) {
        return serviceCenterService.create(request);
    }

    /**
     * Cập nhật Service Center theo ID.
     */
    @PutMapping("/{id}")
    public ServiceCenterResponse update(@PathVariable Integer id,
                                        @RequestBody ServiceCenterRequest request) {
        return serviceCenterService.update(id, request);
    }

    /**
     * Xóa Service Center theo ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        serviceCenterService.delete(id);
    }

    /**
     * Lấy tất cả Service Center.
     */
    @GetMapping
    public List<ServiceCenterResponse> getAll() {
        return serviceCenterService.getAll();
    }

    /**
     * Lấy Service Center theo ID.
     */
    @GetMapping("/{id}")
    public ServiceCenterResponse getById(@PathVariable Integer id) {
        return serviceCenterService.getById(id);
    }

    /**
     * Tìm kiếm theo tên trung tâm.
     * /api/service-centers/search/name?value=Honda
     */
    @GetMapping("/search/name")
    public List<ServiceCenterResponse> getByName(@RequestParam("value") String name) {
        return serviceCenterService.getByName(name);
    }

    /**
     * Tìm kiếm theo location.
     * /api/service-centers/search/location?value=HCM
     */
    @GetMapping("/search/location")
    public List<ServiceCenterResponse> getByLocation(@RequestParam("value") String location) {
        return serviceCenterService.getByLocation(location);
    }
}
