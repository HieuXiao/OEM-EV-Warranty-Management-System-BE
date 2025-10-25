package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.PartUnderWarranty;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.PartUnderWarrantyRequest;
import com.mega.warrantymanagementsystem.model.response.PartUnderWarrantyResponse;
import com.mega.warrantymanagementsystem.repository.AccountRepository;
import com.mega.warrantymanagementsystem.repository.PartUnderWarrantyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartUnderWarrantyService {

    @Autowired
    private PartUnderWarrantyRepository partRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public PartUnderWarrantyResponse createPart(PartUnderWarrantyRequest request) {
        PartUnderWarranty part = modelMapper.map(request, PartUnderWarranty.class);
        Account admin = accountRepo.findById(request.getAdminId().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + request.getAdminId()));
        part.setAdmin(admin);

        PartUnderWarranty saved = partRepo.save(part);
        return mapToResponse(saved);
    }

    // READ ALL
    public List<PartUnderWarrantyResponse> getAllParts() {
        return partRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // READ BY SERIAL
    public PartUnderWarrantyResponse getPartBySerial(String serial) {
        PartUnderWarranty part = partRepo.findById(serial)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + serial));
        return mapToResponse(part);
    }

    // UPDATE
    public PartUnderWarrantyResponse updatePart(String serial, PartUnderWarrantyRequest request) {
        PartUnderWarranty part = partRepo.findById(serial)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + serial));

        part.setPartName(request.getPartName());
        part.setPartBrand(request.getPartBrand());
        part.setPrice(request.getPrice());
        part.setVehicleModel(request.getVehicleModel());
        part.setDescription(request.getDescription());
        part.setIsEnable(request.getIsEnable());

        if (request.getAdminId() != null) {
            Account admin = accountRepo.findById(request.getAdminId().toUpperCase())
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + request.getAdminId()));
            part.setAdmin(admin);
        }

        return mapToResponse(partRepo.save(part));
    }

    // DELETE
    public void deletePart(String serial) {
        PartUnderWarranty part = partRepo.findById(serial)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + serial));
        partRepo.delete(part);
    }

    // mapping entity -> response
    private PartUnderWarrantyResponse mapToResponse(PartUnderWarranty part) {
        PartUnderWarrantyResponse res = modelMapper.map(part, PartUnderWarrantyResponse.class);
        if (part.getAdmin() != null) {
            res.setAdmin(modelMapper.map(part.getAdmin(), res.getAdmin() != null ? res.getAdmin().getClass() : res.getAdmin().getClass()));
        }
        return res;
    }
}
