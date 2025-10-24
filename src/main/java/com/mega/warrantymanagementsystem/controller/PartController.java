package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.Part;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.PartRequest;
import com.mega.warrantymanagementsystem.model.response.PartResponse;
import com.mega.warrantymanagementsystem.service.PartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@CrossOrigin
@SecurityRequirement(name = "api")
public class PartController {

    @Autowired
    private PartService partService;

    //------------------Get All Parts------------------------
    @GetMapping("/")
    public ResponseEntity<List<PartResponse>> getAllParts() {
        return ResponseEntity.ok(partService.getAllParts());
    }

    //------------------Get Part By Id------------------------
    @GetMapping("/{partId}getByID")
    public ResponseEntity<PartResponse> getPartById(@PathVariable("partId") int partId) {
        PartResponse part = partService.getPartById(partId);
        if (part == null) {
            throw new ResourceNotFoundException("Part not found with ID: " + partId);
        }
        return ResponseEntity.ok(part);
    }

    //------------------Create Part------------------------
    @PostMapping("/create")
    public ResponseEntity<Part> createPart(@RequestBody PartRequest partRequest) {
        Part newPart = partService.createPart(partRequest);
        return ResponseEntity.ok(newPart);
    }

    //------------------Update Part------------------------
    @PutMapping("/{partId}update")
    public ResponseEntity<Part> updatePart(
            @PathVariable("partId") int partId,
            @RequestBody PartRequest partRequest) {
        Part updatedPart = partService.updatePart(partId, partRequest);
        return ResponseEntity.ok(updatedPart);
    }

    //------------------Delete Part------------------------
    @DeleteMapping("/{partId}delete")
    public ResponseEntity<Void> deletePart(@PathVariable("partId") int partId) {
        partService.deletePart(partId);
        return ResponseEntity.noContent().build();
    }
}
