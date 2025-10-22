package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.ClaimReplacementPart;
import com.mega.warrantymanagementsystem.entity.Part;
import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ClaimReplacementPartRequest;
import com.mega.warrantymanagementsystem.model.response.ClaimReplacementPartResponse;
import com.mega.warrantymanagementsystem.repository.ClaimReplacementPartRepository;
import com.mega.warrantymanagementsystem.repository.PartRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import com.mega.warrantymanagementsystem.service.ClaimReplacementPartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClaimReplacementPartServiceImpl implements ClaimReplacementPartService {

    @Autowired
    ClaimReplacementPartRepository claimReplacementPartRepository;

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ClaimReplacementPartResponse findById(int id) {
        ClaimReplacementPart part = claimReplacementPartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClaimReplacementPart not found with ID: " + id));
        return mapToResponse(part);
    }

    @Override
    public List<ClaimReplacementPartResponse> findAll() {
        List<ClaimReplacementPart> claimReplacementParts = claimReplacementPartRepository.findAll();
        List<ClaimReplacementPartResponse> claimReplacementPartResponses = new ArrayList<>();
        for(ClaimReplacementPart c : claimReplacementParts){
            claimReplacementPartResponses.add(mapToResponse(c));
        }
        return claimReplacementPartResponses;
    }

    @Override
    public ClaimReplacementPartResponse createClaimReplacementPart(ClaimReplacementPartRequest request) {

        // Chống trùng ID (primary key auto, nhưng vẫn giữ kiểm tra)
        if (request.getPartId() == null) {
            throw new ResourceNotFoundException("Part ID cannot be null");
        }

        ClaimReplacementPart entity = new ClaimReplacementPart();

        Part part = partRepository.findById(request.getPartId())
                .orElseThrow(() -> new ResourceNotFoundException("Part not found with ID: " + request.getPartId()));
        entity.setPart(part);
        entity.setQuantity(request.getQuantity());
        entity.setReason(request.getReason());
        entity.setDescription(request.getDescription());

        // Nếu có claim ID thì gắn luôn
        if (request.getClaimId() != null) {
            WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                    .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found with ID: " + request.getClaimId()));
            entity.setWarrantyClaim(claim);
        }

        ClaimReplacementPart saved = claimReplacementPartRepository.save(entity);
        return mapToResponse(saved);
    }

    @Override
    public ClaimReplacementPartResponse updateClaimReplacementPart(int id, ClaimReplacementPartRequest request) {
        ClaimReplacementPart existing = claimReplacementPartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClaimReplacementPart not found with ID: " + id));

        if (request.getPartId() != null) {
            Part part = partRepository.findById(request.getPartId())
                    .orElseThrow(() -> new ResourceNotFoundException("Part not found with ID: " + request.getPartId()));
            existing.setPart(part);
        }

        if (request.getClaimId() != null) {
            WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                    .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found with ID: " + request.getClaimId()));
            existing.setWarrantyClaim(claim);
        }

        existing.setQuantity(request.getQuantity());
        existing.setReason(request.getReason());
        existing.setDescription(request.getDescription());

        ClaimReplacementPart updated = claimReplacementPartRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void deleteClaimReplacementPart(int id) {
        ClaimReplacementPart existing = claimReplacementPartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClaimReplacementPart not found with ID: " + id));
        claimReplacementPartRepository.delete(existing);
    }

    private ClaimReplacementPartResponse mapToResponse(ClaimReplacementPart entity) {
        ClaimReplacementPartResponse response = new ClaimReplacementPartResponse();
        response.setPartUserId(entity.getPartUserId());
        response.setQuantity(entity.getQuantity());
        response.setReason(entity.getReason());
        response.setDescription(entity.getDescription());

        if (entity.getPart() != null) {
            response.setPartId(entity.getPart().getPartId());
        }
        if (entity.getWarrantyClaim() != null) {
            response.setClaimId(entity.getWarrantyClaim().getClaimId());
        }

        return response;
    }

}
