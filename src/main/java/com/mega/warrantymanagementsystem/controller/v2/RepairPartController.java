package com.mega.warrantymanagementsystem.controller.v2;

import com.mega.warrantymanagementsystem.model.response.PartResponse;
import com.mega.warrantymanagementsystem.service.v2.RepairPartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repair-parts")
@CrossOrigin
@SecurityRequirement(name = "api")
public class RepairPartController {

    @Autowired
    private RepairPartService repairPartService;

    // Khi claim chuyển sang REPAIR thì gọi hàm này
    @PostMapping("/process/{warrantyId}")
    public ResponseEntity<String> processRepairParts(@PathVariable String warrantyId) {
        repairPartService.handleRepairParts(warrantyId);
        return ResponseEntity.ok("Parts updated successfully for claim " + warrantyId);
    }

    // API bổ sung Part quantity cho warehouse cụ thể
    @PatchMapping("/add-quantity")
    public ResponseEntity<PartResponse> addQuantity(
            @RequestParam String partNumber,
            @RequestParam int quantity,
            @RequestParam int warehouseId
    ) {
        return ResponseEntity.ok(repairPartService.addQuantity(partNumber, quantity, warehouseId));
    }
}
