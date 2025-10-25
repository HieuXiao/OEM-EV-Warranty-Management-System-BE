package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.PartUnderWarrantyRequest;
import com.mega.warrantymanagementsystem.model.response.PartUnderWarrantyResponse;
import com.mega.warrantymanagementsystem.service.PartUnderWarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/part-under-warranty-controller")
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

    @GetMapping("/{serial}")
    public PartUnderWarrantyResponse getPart(@PathVariable String serial) {
        return partService.getPartBySerial(serial);
    }

    @PutMapping("/{serial}")
    public PartUnderWarrantyResponse updatePart(@PathVariable String serial, @RequestBody PartUnderWarrantyRequest request) {
        return partService.updatePart(serial, request);
    }

    @DeleteMapping("/{serial}")
    public void deletePart(@PathVariable String serial) {
        partService.deletePart(serial);
    }
}
