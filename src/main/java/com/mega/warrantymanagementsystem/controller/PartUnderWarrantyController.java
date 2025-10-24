package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.PartUnderWarranty;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.PartUnderWarrantyRequest;
import com.mega.warrantymanagementsystem.model.response.PartUnderWarrantyResponse;
import com.mega.warrantymanagementsystem.service.PartUnderWarrantyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts-under-warranty")
@CrossOrigin
@SecurityRequirement(name = "api")
public class PartUnderWarrantyController {

    @Autowired
    private PartUnderWarrantyService partUnderWarrantyService;

    //------------------Get All Parts Under Warranty------------------------
    @GetMapping("/")
    public ResponseEntity<List<PartUnderWarrantyResponse>> getAllPartsUnderWarranty() {
        return ResponseEntity.ok(partUnderWarrantyService.getAllPartsUnderWarranty());
    }

    //------------------Get Part Under Warranty By Serial------------------------
    @GetMapping("/{partSerial}")
    public ResponseEntity<PartUnderWarrantyResponse> getPartBySerial(@PathVariable("partSerial") String partSerial) {
        PartUnderWarrantyResponse part = partUnderWarrantyService.getPartBySerial(partSerial);
        if (part == null) {
            throw new ResourceNotFoundException("Part under warranty not found with serial: " + partSerial);
        }
        return ResponseEntity.ok(part);
    }

    //------------------Create Part Under Warranty------------------------
    @PostMapping("/create")
    public ResponseEntity<PartUnderWarranty> createPartUnderWarranty(
            @RequestBody PartUnderWarrantyRequest request) {

        // lấy người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account currentUser = (Account) authentication.getPrincipal();

        // kiểm tra quyền admin
        if (!currentUser.getRole().getRoleName().name().equalsIgnoreCase("ADMIN")) {
            throw new IllegalArgumentException("You are not authorized to create part");
        }

        // gọi service để tạo part
        PartUnderWarranty newPart = partUnderWarrantyService.createPartUnderWarranty(request);

        // gán thông tin admin tạo
        newPart.setAdmin(currentUser);

        // lưu lại sau khi gán admin
        partUnderWarrantyService.save(newPart);

        return ResponseEntity.ok(newPart);
    }

    //------------------Update Part Under Warranty------------------------
    @PutMapping("/{partSerial}/update")
    public ResponseEntity<PartUnderWarranty> updatePartUnderWarranty(
            @PathVariable("partSerial") String partSerial,
            @RequestBody PartUnderWarrantyRequest request) {
        PartUnderWarranty updatedPart = partUnderWarrantyService.updatePartUnderWarranty(partSerial, request);
        return ResponseEntity.ok(updatedPart);
    }

    //------------------Delete Part Under Warranty------------------------
    @DeleteMapping("/{partSerial}/delete")
    public ResponseEntity<Void> deletePartUnderWarranty(@PathVariable("partSerial") String partSerial) {
        partUnderWarrantyService.deletePartUnderWarranty(partSerial);
        return ResponseEntity.noContent().build();
    }
}
