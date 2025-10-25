//package com.mega.warrantymanagementsystem.controller;
//
//import com.mega.warrantymanagementsystem.entity.Inventory;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.InventoryRequest;
//import com.mega.warrantymanagementsystem.model.response.InventoryResponse;
//import com.mega.warrantymanagementsystem.service.InventoryService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/inventories")
//@CrossOrigin
//@SecurityRequirement(name = "api")
//public class InventoryController {
//
//    @Autowired
//    private InventoryService inventoryService;
//
//    //------------------Get All Inventories------------------------
//    @GetMapping("/")
//    public ResponseEntity<List<InventoryResponse>> getAllInventories() {
//        return ResponseEntity.ok(inventoryService.getInventories());
//    }
//
//    //------------------Get Inventory By Id------------------------
//    @GetMapping("/{inventoryId}getByID")
//    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable("inventoryId") int inventoryId) {
//        InventoryResponse inventory = inventoryService.getInventoryById(inventoryId);
//        if (inventory == null) {
//            throw new ResourceNotFoundException("Inventory not found with ID: " + inventoryId);
//        }
//        return ResponseEntity.ok(inventory);
//    }
//
//    //------------------Create Inventory------------------------
//    @PostMapping("/create")
//    public ResponseEntity<Inventory> createInventory(@RequestBody InventoryRequest inventoryRequest) {
//        Inventory newInventory = inventoryService.createInventory(inventoryRequest);
//        return ResponseEntity.ok(newInventory);
//    }
//
//    //------------------Update Inventory------------------------
//    @PutMapping("/{inventoryId}update")
//    public ResponseEntity<Inventory> updateInventory(
//            @PathVariable("inventoryId") int inventoryId,
//            @RequestBody InventoryRequest inventoryRequest) {
//
//        Inventory updatedInventory = inventoryService.updateInventory(inventoryId, inventoryRequest);
//        return ResponseEntity.ok(updatedInventory);
//    }
//
//    //------------------Delete Inventory------------------------
//    @DeleteMapping("/{inventoryId}delete")
//    public ResponseEntity<Void> deleteInventory(@PathVariable("inventoryId") int inventoryId) {
//        inventoryService.deleteInventory(inventoryId);
//        return ResponseEntity.noContent().build();
//    }
//}
