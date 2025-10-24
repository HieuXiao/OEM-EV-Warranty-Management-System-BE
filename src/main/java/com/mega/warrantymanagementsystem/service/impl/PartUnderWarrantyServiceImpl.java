package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.PartUnderWarranty;
import com.mega.warrantymanagementsystem.model.request.PartUnderWarrantyRequest;
import com.mega.warrantymanagementsystem.model.response.PartUnderWarrantyResponse;
import com.mega.warrantymanagementsystem.repository.PartUnderWarrantyRepository;
import com.mega.warrantymanagementsystem.service.PartUnderWarrantyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartUnderWarrantyServiceImpl implements PartUnderWarrantyService {

    @Autowired
    private PartUnderWarrantyRepository partUnderWarrantyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PartUnderWarranty save(PartUnderWarranty part) {
        return partUnderWarrantyRepository.save(part);
    }

    //------------------Create Part Under Warranty------------------------
    @Override
    public PartUnderWarranty createPartUnderWarranty(PartUnderWarrantyRequest request) {
        if (partUnderWarrantyRepository.existsByPartSerial(request.getPartSerial())) {
            throw new IllegalArgumentException("Part with this serial already exists!");
        }

        PartUnderWarranty part = modelMapper.map(request, PartUnderWarranty.class);
        return partUnderWarrantyRepository.save(part);
    }

    //------------------Update Part Under Warranty------------------------
    @Override
    public PartUnderWarranty updatePartUnderWarranty(String partSerial, PartUnderWarrantyRequest request) {
        Optional<PartUnderWarranty> optionalPart = partUnderWarrantyRepository.findById(partSerial);

        if (optionalPart.isPresent()) {
            PartUnderWarranty existingPart = optionalPart.get();
            existingPart.setPartName(request.getPartName());
            existingPart.setPartBranch(request.getPartBranch());
            existingPart.setPrice(request.getPrice());
            existingPart.setVehicleModel(request.getVehicleModel());
            existingPart.setDescription(request.getDescription());
            return partUnderWarrantyRepository.save(existingPart);
        } else {
            throw new IllegalArgumentException("Part not found with serial: " + partSerial);
        }
    }

    //------------------Delete Part Under Warranty------------------------
    @Override
    public void deletePartUnderWarranty(String partSerial) {
        if (!partUnderWarrantyRepository.existsById(partSerial)) {
            throw new IllegalArgumentException("Part not found with serial: " + partSerial);
        }
        partUnderWarrantyRepository.deleteById(partSerial);
    }

    //------------------Get All Parts Under Warranty------------------------
    @Override
    public List<PartUnderWarrantyResponse> getAllPartsUnderWarranty() {
        List<PartUnderWarranty> parts = partUnderWarrantyRepository.findAll();
        return parts.stream()
                .map(part -> modelMapper.map(part, PartUnderWarrantyResponse.class))
                .collect(Collectors.toList());
    }

    //------------------Get Part By Serial------------------------
    @Override
    public PartUnderWarrantyResponse getPartBySerial(String partSerial) {
        PartUnderWarranty part = partUnderWarrantyRepository.findById(partSerial)
                .orElseThrow(() -> new IllegalArgumentException("Part not found with serial: " + partSerial));
        return modelMapper.map(part, PartUnderWarrantyResponse.class);
    }
}
