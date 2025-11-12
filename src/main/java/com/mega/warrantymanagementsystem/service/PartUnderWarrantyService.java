package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.PartUnderWarranty;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
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

    @Autowired
    private PartService partService;

    // CREATE
    public PartUnderWarrantyResponse createPart(PartUnderWarrantyRequest request) {
        // kiểm tra trùng id
        if (partRepo.existsById(request.getPartId())) {
            throw new DuplicateResourceException("Part ID already exists: " + request.getPartId());
        }

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

    // READ BY ID
    public PartUnderWarrantyResponse getPartById(String partId) {
        PartUnderWarranty part = partRepo.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + partId));
        return mapToResponse(part);
    }

    // UPDATE
    public PartUnderWarrantyResponse updatePart(String partId, PartUnderWarrantyRequest request) {
        PartUnderWarranty part = partRepo.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + partId));

        Boolean oldStatus = part.getIsEnable();

        part.setPartName(request.getPartName());
        part.setPartBrand(request.getPartBrand());
        part.setPrice(request.getPrice());
        part.setVehicleModel(request.getVehicleModel());
        part.setDescription(request.getDescription());
        part.setIsEnable(request.getIsEnable());

        PartUnderWarranty saved = partRepo.save(part);

        // Nếu trạng thái thay đổi
        if (oldStatus != null && !oldStatus.equals(request.getIsEnable())) {
            if (Boolean.FALSE.equals(request.getIsEnable())) {
                partService.disablePart(partId); // Ẩn tất cả part liên quan
            } else if (Boolean.TRUE.equals(request.getIsEnable())) {
                partService.enablePart(partId); // Hiện lại
            }
        }

        if (request.getAdminId() != null) {
            Account admin = accountRepo.findById(request.getAdminId().toUpperCase())
                    .orElseThrow(() -> new ResourceNotFoundException("Admin not found: " + request.getAdminId()));
            part.setAdmin(admin);
        }

        return mapToResponse(partRepo.save(part));
    }

    // DELETE
    public void deletePart(String partId) {
        PartUnderWarranty part = partRepo.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + partId));
        partRepo.delete(part);
    }

    // UPDATE ENABLE STATUS ONLY
    public PartUnderWarrantyResponse updateEnableStatus(String partId, Boolean isEnable) {
        PartUnderWarranty part = partRepo.findById(partId)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + partId));

        Boolean oldStatus = part.getIsEnable();
        part.setIsEnable(isEnable);
        PartUnderWarranty saved = partRepo.save(part);

        // Nếu trạng thái thay đổi thì ẩn hoặc hiện lại các part liên quan
        if (oldStatus != null && !oldStatus.equals(isEnable)) {
            if (Boolean.FALSE.equals(isEnable)) {
                partService.disablePart(partId);
            } else if (Boolean.TRUE.equals(isEnable)) {
                partService.enablePart(partId);
            }
        }

        return mapToResponse(saved);
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
