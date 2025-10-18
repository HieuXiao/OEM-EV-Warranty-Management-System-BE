package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.ClaimAttachment;
import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ClaimAttachmentRequest;
import com.mega.warrantymanagementsystem.model.response.ClaimAttachmentResponse;
import com.mega.warrantymanagementsystem.repository.ClaimAttachmentRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import com.mega.warrantymanagementsystem.service.ClaimAttachmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClaimAttachmentServiceImpl implements ClaimAttachmentService {

    @Autowired
    private ClaimAttachmentRepository claimAttachmentRepository;

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private ModelMapper modelMapper;

    // ---------------- FIND BY ID ----------------
    @Override
    public ClaimAttachmentResponse findByAttachmentId(int id) {
        ClaimAttachment attachment = claimAttachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClaimAttachment not found with ID: " + id));

        return mapToResponse(attachment);
    }

    // ---------------- FIND ALL ----------------
    @Override
    public List<ClaimAttachmentResponse> findAllAttachment() {
        List<ClaimAttachment> attachments = claimAttachmentRepository.findAll();
        List<ClaimAttachmentResponse> responses = new ArrayList<>();

        for (ClaimAttachment attachment : attachments) {
            responses.add(mapToResponse(attachment));
        }
        return responses;
    }

    // ---------------- CREATE ----------------
    @Override
    public ClaimAttachmentResponse createClaimAttachment(ClaimAttachmentRequest request) {

        ClaimAttachment existingType = claimAttachmentRepository.findByFileType(request.getFileType());
        if (existingType != null) {
            throw new DuplicateResourceException("ClaimAttachment with fileType already exists: " + request.getFileType());
        }

        ClaimAttachment existingPath = claimAttachmentRepository.findByFilePath(request.getFilePath());
        if (existingPath != null) {
            throw new DuplicateResourceException("ClaimAttachment with filePath already exists: " + request.getFilePath());
        }

        ClaimAttachment attachment = new ClaimAttachment();
        attachment.setFileType(request.getFileType());
        attachment.setFilePath(request.getFilePath());

        // Gán claim nếu client truyền claimId
        if (request.getClaimId() != null) {
            WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                    .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found with ID: " + request.getClaimId()));
            attachment.setWarrantyClaim(claim);
        }

        ClaimAttachment saved = claimAttachmentRepository.save(attachment);
        return mapToResponse(saved);
    }

    // ---------------- UPDATE ----------------
    @Override
    public ClaimAttachmentResponse updateClaimAttachment(int attachmentId, ClaimAttachmentRequest request) {
        ClaimAttachment existing = claimAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("ClaimAttachment not found with ID: " + attachmentId));

        existing.setFileType(request.getFileType());
        existing.setFilePath(request.getFilePath());

        if (request.getClaimId() != null) {
            WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                    .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found with ID: " + request.getClaimId()));
            existing.setWarrantyClaim(claim);
        }

        ClaimAttachment updated = claimAttachmentRepository.save(existing);
        return mapToResponse(updated);
    }

    // ---------------- DELETE ----------------
    @Override
    public void deleteClaimAttachment(int attachmentId) {
        ClaimAttachment existing = claimAttachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("ClaimAttachment not found with ID: " + attachmentId));

        claimAttachmentRepository.delete(existing);
    }

    // ---------------- MAPPER ----------------
    private ClaimAttachmentResponse mapToResponse(ClaimAttachment attachment) {
        ClaimAttachmentResponse response = new ClaimAttachmentResponse();
        response.setAttachmentId(attachment.getAttachmentId());
        response.setFileType(attachment.getFileType());
        response.setFilePath(attachment.getFilePath());

        if (attachment.getWarrantyClaim() != null) {
            response.setClaimId(attachment.getWarrantyClaim().getClaimId());
        } else {
            response.setClaimId(null);
        }

        return response;
    }
}
