package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.PartRequest;
import com.mega.warrantymanagementsystem.model.response.PartResponse;
import com.mega.warrantymanagementsystem.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    @Autowired
    private PartService partService;

    @GetMapping
    public List<PartResponse> getAllParts() {
        return partService.getAllParts();
    }

    @GetMapping("/{partSerial}")
    public PartResponse getPart(@PathVariable String partSerial) {
        return partService.getPartBySerial(partSerial);
    }

    @PostMapping
    public PartResponse createPart(@RequestBody PartRequest request) {
        return partService.createPart(request);
    }

    @PutMapping("/{partSerial}")
    public PartResponse updatePart(@PathVariable String partSerial, @RequestBody PartRequest request) {
        return partService.updatePart(partSerial, request);
    }

    @DeleteMapping("/{partSerial}")
    public String deletePart(@PathVariable String partSerial) {
        partService.deletePart(partSerial);
        return "Deleted part: " + partSerial;
    }
}
