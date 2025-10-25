//package com.mega.warrantymanagementsystem.service.impl;
//
//import com.mega.warrantymanagementsystem.entity.WarrantyFile;
//import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
//import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.ClaimAttachmentRequest;
//import com.mega.warrantymanagementsystem.model.response.ClaimAttachmentResponse;
//import com.mega.warrantymanagementsystem.repository.ClaimAttachmentRepository;
//import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
//import com.mega.warrantymanagementsystem.service.ClaimAttachmentService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ClaimAttachmentServiceImpl implements ClaimAttachmentService {
//
//    @Autowired
//    private ClaimAttachmentRepository claimAttachmentRepository;
//
//    @Autowired
//    private WarrantyClaimRepository warrantyClaimRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    // ---------------- FIND BY ID ----------------
//    @Override
//    public ClaimAttachmentResponse findByAttachmentId(int id) {
//        WarrantyFile attachment = claimAttachmentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("WarrantyFile not found with ID: " + id));
//
//        return mapToResponse(attachment);
//    }
//
//    // ---------------- FIND ALL ----------------
//    @Override
//    public List<ClaimAttachmentResponse> findAllAttachment() {
//        List<WarrantyFile> attachments = claimAttachmentRepository.findAll();
//        List<ClaimAttachmentResponse> responses = new ArrayList<>();
//
//        for (WarrantyFile attachment : attachments) {
//            responses.add(mapToResponse(attachment));
//        }
//        return responses;
//    }
//
//    // ---------------- CREATE ----------------
//    @Override
//    public ClaimAttachmentResponse createClaimAttachment(ClaimAttachmentRequest request) {
//
//        WarrantyFile existingType = claimAttachmentRepository.findByFileType(request.getFileType());
//        if (existingType != null) {
//            throw new DuplicateResourceException("WarrantyFile with fileType already exists: " + request.getFileType());
//        }
//
//        WarrantyFile existingPath = claimAttachmentRepository.findByFilePath(request.getFilePath());
//        if (existingPath != null) {
//            throw new DuplicateResourceException("WarrantyFile with filePath already exists: " + request.getFilePath());
//        }
//
//        WarrantyFile attachment = new WarrantyFile();
//        attachment.setFileType(request.getFileType());
//        attachment.setFilePath(request.getFilePath());
//
//        // Gán claim nếu client truyền claimId
//        if (request.getClaimId() != null) {
//            WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
//                    .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found with ID: " + request.getClaimId()));
//            attachment.setWarrantyClaim(claim);
//        }
//
//        WarrantyFile saved = claimAttachmentRepository.save(attachment);
//        return mapToResponse(saved);
//    }
//
//    // ---------------- UPDATE ----------------
//    @Override
//    public ClaimAttachmentResponse updateClaimAttachment(int attachmentId, ClaimAttachmentRequest request) {
//        WarrantyFile existing = claimAttachmentRepository.findById(attachmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("WarrantyFile not found with ID: " + attachmentId));
//
//        existing.setFileType(request.getFileType());
//        existing.setFilePath(request.getFilePath());
//
//        if (request.getClaimId() != null) {
//            WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
//                    .orElseThrow(() -> new ResourceNotFoundException("WarrantyClaim not found with ID: " + request.getClaimId()));
//            existing.setWarrantyClaim(claim);
//        }
//
//        WarrantyFile updated = claimAttachmentRepository.save(existing);
//        return mapToResponse(updated);
//    }
//
//    // ---------------- DELETE ----------------
//    @Override
//    public void deleteClaimAttachment(int attachmentId) {
//        WarrantyFile existing = claimAttachmentRepository.findById(attachmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("WarrantyFile not found with ID: " + attachmentId));
//
//        claimAttachmentRepository.delete(existing);
//    }
//
//    // ---------------- MAPPER ----------------
//    private ClaimAttachmentResponse mapToResponse(WarrantyFile attachment) {
//        ClaimAttachmentResponse response = new ClaimAttachmentResponse();
//        response.setAttachmentId(attachment.getAttachmentId());
//        response.setFileType(attachment.getFileType());
//        response.setFilePath(attachment.getFilePath());
//
//        if (attachment.getWarrantyClaim() != null) {
//            response.setClaimId(attachment.getWarrantyClaim().getClaimId());
//        } else {
//            response.setClaimId(null);
//        }
//
//        return response;
//    }
//}
