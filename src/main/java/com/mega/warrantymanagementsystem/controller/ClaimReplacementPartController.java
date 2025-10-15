package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.ClaimReplacementPart;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ClaimReplacementPartRequest;
import com.mega.warrantymanagementsystem.model.response.ClaimReplacementPartResponse;
import com.mega.warrantymanagementsystem.service.ClaimReplacementPartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//biểu thị đây là controller
@RequestMapping("/api/claim_replacement_parts")//đường dẫn chung
@CrossOrigin//cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")
public class ClaimReplacementPartController {

    @Autowired
    ClaimReplacementPartService claimReplacementPartService;

    @GetMapping("/{id}")
    public ResponseEntity<ClaimReplacementPartResponse> getById(@PathVariable int id) {
        return ResponseEntity.ok(claimReplacementPartService.findById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<ClaimReplacementPartResponse>> getAllClaimReplacementPart (){
        return ResponseEntity.ok(claimReplacementPartService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<ClaimReplacementPartResponse> create(@Valid @RequestBody ClaimReplacementPartRequest request) {
        return ResponseEntity.ok(claimReplacementPartService.createClaimReplacementPart(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClaimReplacementPartResponse> update(
            @PathVariable int id,
            @Valid @RequestBody ClaimReplacementPartRequest request) {
        return ResponseEntity.ok(claimReplacementPartService.updateClaimReplacementPart(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        claimReplacementPartService.deleteClaimReplacementPart(id);
        return ResponseEntity.noContent().build();
    }

}
