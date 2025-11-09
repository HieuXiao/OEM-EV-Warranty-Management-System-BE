package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.PartRequest;
import com.mega.warrantymanagementsystem.model.response.PartResponse;
import com.mega.warrantymanagementsystem.service.PartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/parts")
@CrossOrigin
@SecurityRequirement(name = "api")
public class PartController {

    @Autowired
    private PartService partService;

    @GetMapping
    public List<PartResponse> getAllParts() {
        return partService.getAllParts();
    }

    @GetMapping("/{serial}")
    public PartResponse getPart(@PathVariable String serial) {
        return partService.getPartBySerial(serial);
    }

    @PostMapping
    public PartResponse createPart(@RequestBody PartRequest req) {
        return partService.createPart(req);
    }

    @PutMapping("/{serial}")
    public PartResponse updatePart(@PathVariable String serial, @RequestBody PartRequest req) {
        return partService.updatePart(serial, req);
    }

    @DeleteMapping("/{serial}")
    public String deletePart(@PathVariable String serial) {
        partService.deletePart(serial);
        return "Deleted part: " + serial;
    }
}
