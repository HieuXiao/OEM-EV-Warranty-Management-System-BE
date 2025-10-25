//package com.mega.warrantymanagementsystem.service.impl;
//
//import com.mega.warrantymanagementsystem.entity.Inventory;
//import com.mega.warrantymanagementsystem.model.request.InventoryRequest;
//import com.mega.warrantymanagementsystem.model.response.InventoryResponse;
//import com.mega.warrantymanagementsystem.repository.InventoryRepository;
//import com.mega.warrantymanagementsystem.service.InventoryService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class InventoryServiceImpl implements InventoryService {
//
//    @Autowired
//    private InventoryRepository inventoryRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    //------------------Create Inventory------------------------
//    @Override
//    public Inventory createInventory(InventoryRequest inventoryRequest) {
//        Inventory inventory = modelMapper.map(inventoryRequest, Inventory.class);
//        return inventoryRepository.save(inventory);
//    }
//
//    //------------------Update Inventory------------------------
//    @Override
//    public Inventory updateInventory(int inventoryId, InventoryRequest inventoryRequest) {
//        Optional<Inventory> optionalInventory = inventoryRepository.findById(inventoryId);
//
//        if (optionalInventory.isPresent()) {
//            Inventory existingInventory = optionalInventory.get();
//            existingInventory.setLocation(inventoryRequest.getLocation());
//            return inventoryRepository.save(existingInventory);
//        } else {
//            throw new IllegalArgumentException("Inventory not found with id: " + inventoryId);
//        }
//    }
//
//    //------------------Delete Inventory------------------------
//    @Override
//    public void deleteInventory(int inventoryId) {
//        if (!inventoryRepository.existsById(inventoryId)) {
//            throw new IllegalArgumentException("Inventory not found with id: " + inventoryId);
//        }
//        inventoryRepository.deleteById(inventoryId);
//    }
//
//    //------------------Get All Inventories------------------------
//    @Override
//    public List<InventoryResponse> getInventories() {
//        List<Inventory> inventories = inventoryRepository.findAll();
//        return inventories.stream()
//                .map(inventory -> modelMapper.map(inventory, InventoryResponse.class))
//                .collect(Collectors.toList());
//    }
//
//    //------------------Get Inventory By Id------------------------
//    @Override
//    public InventoryResponse getInventoryById(int inventoryId) {
//        Inventory inventory = inventoryRepository.findById(inventoryId)
//                .orElseThrow(() -> new IllegalArgumentException("Inventory not found with id: " + inventoryId));
//        return modelMapper.map(inventory, InventoryResponse.class);
//    }
//}
