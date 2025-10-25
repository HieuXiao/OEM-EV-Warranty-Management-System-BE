//package com.mega.warrantymanagementsystem.service.impl;
//
//import com.mega.warrantymanagementsystem.entity.Inventory;
//import com.mega.warrantymanagementsystem.entity.Part;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.PartRequest;
//import com.mega.warrantymanagementsystem.model.response.PartResponse;
//import com.mega.warrantymanagementsystem.repository.InventoryRepository;
//import com.mega.warrantymanagementsystem.repository.PartRepository;
//import com.mega.warrantymanagementsystem.service.PartService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class PartServiceImpl implements PartService {
//
//    @Autowired
//    private PartRepository partRepository;
//
//    @Autowired
//    private InventoryRepository inventoryRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    //------------------Create new part------------------------
//    @Override
//    public Part createPart(PartRequest partRequest) {
//        if (partRepository.existsBySerialNumber(partRequest.getSerialNumber())) {
//            throw new RuntimeException("Serial number already exists!");
//        }
//
//        Inventory inventory = inventoryRepository.findById(partRequest.getInventoryId())
//                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + partRequest.getInventoryId()));
//
//        Part part = modelMapper.map(partRequest, Part.class);
//        part.setInventoryId(inventory);
//        return partRepository.save(part);
//    }
//
//    //------------------Update existing part------------------------
//    @Override
//    public Part updatePart(int partId, PartRequest partRequest) {
//        Part part = partRepository.findById(partId)
//                .orElseThrow(() -> new ResourceNotFoundException("Part not found with ID: " + partId));
//
//        Inventory inventory = inventoryRepository.findById(partRequest.getInventoryId())
//                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + partRequest.getInventoryId()));
//
//        part.setName(partRequest.getName());
//        part.setSerialNumber(partRequest.getSerialNumber());
//        part.setDescription(partRequest.getDescription());
//        part.setInventoryId(inventory);
//
//        return partRepository.save(part);
//    }
//
//    //------------------Delete part------------------------
//    @Override
//    public void deletePart(int partId) {
//        if (!partRepository.existsById(partId)) {
//            throw new ResourceNotFoundException("Part not found with ID: " + partId);
//        }
//        partRepository.deleteById(partId);
//    }
//
//    //------------------Get part by ID------------------------
//    @Override
//    public PartResponse getPartById(int partId) {
//        Part part = partRepository.findById(partId)
//                .orElseThrow(() -> new ResourceNotFoundException("Part not found with ID: " + partId));
//
//        PartResponse response = modelMapper.map(part, PartResponse.class);
//        response.setInventoryId(part.getInventoryId().getInventoryId());
//        return response;
//    }
//
//    //------------------Get all parts------------------------
//    @Override
//    public List<PartResponse> getAllParts() {
//        return partRepository.findAll()
//                .stream()
//                .map(part -> {
//                    PartResponse response = modelMapper.map(part, PartResponse.class);
//                    response.setInventoryId(part.getInventoryId().getInventoryId());
//                    return response;
//                })
//                .collect(Collectors.toList());
//    }
//}
