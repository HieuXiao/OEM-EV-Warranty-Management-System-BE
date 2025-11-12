package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.PartUnderWarrantyRequest;
import com.mega.warrantymanagementsystem.model.response.PartUnderWarrantyResponse;
import com.mega.warrantymanagementsystem.service.PartUnderWarrantyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/part-under-warranty-controller")
@CrossOrigin//cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")
public class PartUnderWarrantyController {

    @Autowired
    private PartUnderWarrantyService partService;

    @PostMapping
    public PartUnderWarrantyResponse createPart(@RequestBody PartUnderWarrantyRequest request) {
        return partService.createPart(request);
    }

    @GetMapping
    public List<PartUnderWarrantyResponse> getAllParts() {
        return partService.getAllParts();
    }

    @GetMapping("/{id}")
    public PartUnderWarrantyResponse getPart(@PathVariable String id) {
        return partService.getPartById(id);
    }

    @PutMapping("/{id}")
    public PartUnderWarrantyResponse updatePart(@PathVariable String id, @RequestBody PartUnderWarrantyRequest request) {
        return partService.updatePart(id, request);
    }

    @PatchMapping("/{id}/enable")
    public PartUnderWarrantyResponse updateEnableStatus(
            @PathVariable String id,
            @RequestParam Boolean isEnable) {
        return partService.updateEnableStatus(id, isEnable);
    }

    @DeleteMapping("/{serial}")
    public void deletePart(@PathVariable String serial) {
        partService.deletePart(serial);
    }
}
