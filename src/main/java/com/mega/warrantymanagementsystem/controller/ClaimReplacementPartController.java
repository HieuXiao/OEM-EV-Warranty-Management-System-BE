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
    public ResponseEntity<ClaimReplacementPartResponse> getById (@PathVariable int id){
        ClaimReplacementPartResponse claimReplacementPartResponse = claimReplacementPartService.findById(id);
        if(claimReplacementPartResponse == null){
            throw new ResourceNotFoundException("Claim Replacement Part not found with id"+id);
        }
        return ResponseEntity.ok(claimReplacementPartResponse);
    }

    @GetMapping("/")
    public ResponseEntity<List<ClaimReplacementPartResponse>> getAllClaimReplacementPart (){
        return ResponseEntity.ok(claimReplacementPartService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<ClaimReplacementPartResponse> createClaimReplacementPart(
            @PathVariable @Valid ClaimReplacementPartRequest claimReplacementPartRequest){
        ClaimReplacementPartResponse partResponse = claimReplacementPartService.createClaimReplacementPart(claimReplacementPartRequest);
        return ResponseEntity.ok(partResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaimReplacementPart(@PathVariable("id") int id){
        ClaimReplacementPartResponse existing = claimReplacementPartService.findById(id);
        if(existing == null){
            throw new ResourceNotFoundException("Claim Replacement Part not found with id"+id);
        }
        claimReplacementPartService.deleteClaimReplacementPart(id);
        return ResponseEntity.noContent().build();
    }

}
