package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.entity.WarrantyFile;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarrantyFileRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyFileResponse;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarrantyFileService {

    @Autowired
    private WarrantyFileRepository warrantyFileRepository;

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    // --- Tạo mới ---
    public WarrantyFileResponse create(WarrantyFileRequest request) {
        WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim với ID: " + request.getClaimId()));

        WarrantyFile file = new WarrantyFile();
        file.setFileId(request.getFileId());
        file.setWarrantyClaim(claim);
        file.setImageUrl(request.getImageUrl());

        WarrantyFile saved = warrantyFileRepository.save(file);

        return toResponse(saved);
    }

    // --- Cập nhật ---
    public WarrantyFileResponse update(String fileId, WarrantyFileRequest request) {
        WarrantyFile existing = warrantyFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyFile với ID: " + fileId));

        if (request.getClaimId() != null) {
            WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim với ID: " + request.getClaimId()));
            existing.setWarrantyClaim(claim);
        }

        existing.setImageUrl(request.getImageUrl());
        WarrantyFile updated = warrantyFileRepository.save(existing);
        return toResponse(updated);
    }

    // --- Xóa ---
    public void delete(String fileId) {
        if (!warrantyFileRepository.existsById(fileId)) {
            throw new ResourceNotFoundException("Không tìm thấy WarrantyFile với ID: " + fileId);
        }
        warrantyFileRepository.deleteById(fileId);
    }

    // --- Lấy tất cả ---
    public List<WarrantyFileResponse> getAll() {
        return warrantyFileRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // --- Lấy theo fileId ---
    public WarrantyFileResponse getById(String fileId) {
        WarrantyFile file = warrantyFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyFile với ID: " + fileId));
        return toResponse(file);
    }

    // --- Lấy theo claimId ---
    public List<WarrantyFileResponse> getByClaimId(String claimId) {
        return warrantyFileRepository.findAll().stream()
                .filter(f -> f.getWarrantyClaim() != null &&
                        f.getWarrantyClaim().getClaimId().equals(claimId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // --- Convert entity -> response (custom tránh vòng lặp) ---
    private WarrantyFileResponse toResponse(WarrantyFile file) {
        WarrantyFileResponse res = new WarrantyFileResponse();
        res.setFileId(file.getFileId());
        res.setImageUrl(file.getImageUrl());
        res.setClaimId(file.getWarrantyClaim() != null ? file.getWarrantyClaim().getClaimId() : null);
        return res;
    }
}
