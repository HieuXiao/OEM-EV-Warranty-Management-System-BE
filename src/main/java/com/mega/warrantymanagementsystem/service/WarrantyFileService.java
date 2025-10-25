package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.entity.WarrantyFile;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarrantyFileRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyFileResponse;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyFileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý logic cho WarrantyFile.
 * Bao gồm CRUD, tìm kiếm theo fileId và claimId.
 */
@Service
public class WarrantyFileService {

    @Autowired
    private WarrantyFileRepository warrantyFileRepository;

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Tạo mới WarrantyFile.
     */
    public WarrantyFileResponse create(WarrantyFileRequest request) {
        WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim với ID: " + request.getClaimId()));

        WarrantyFile file = new WarrantyFile();
        file.setFileId(request.getFileId());
        file.setWarrantyClaim(claim);
        file.setImageUrl(request.getImageUrl());

        WarrantyFile saved = warrantyFileRepository.save(file);
        return modelMapper.map(saved, WarrantyFileResponse.class);
    }

    /**
     * Cập nhật WarrantyFile theo fileId.
     */
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
        return modelMapper.map(updated, WarrantyFileResponse.class);
    }

    /**
     * Xóa WarrantyFile theo fileId.
     */
    public void delete(String fileId) {
        if (!warrantyFileRepository.existsById(fileId)) {
            throw new ResourceNotFoundException("Không tìm thấy WarrantyFile với ID: " + fileId);
        }
        warrantyFileRepository.deleteById(fileId);
    }

    /**
     * Lấy tất cả WarrantyFile.
     */
    public List<WarrantyFileResponse> getAll() {
        return warrantyFileRepository.findAll().stream()
                .map(w -> modelMapper.map(w, WarrantyFileResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy WarrantyFile theo fileId.
     */
    public WarrantyFileResponse getById(String fileId) {
        WarrantyFile file = warrantyFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyFile với ID: " + fileId));
        return modelMapper.map(file, WarrantyFileResponse.class);
    }

    /**
     * Lấy danh sách file theo claimId.
     */
    public List<WarrantyFileResponse> getByClaimId(String claimId) {
        return warrantyFileRepository.findAll().stream()
                .filter(f -> f.getWarrantyClaim() != null &&
                        f.getWarrantyClaim().getClaimId().equals(claimId))
                .map(f -> modelMapper.map(f, WarrantyFileResponse.class))
                .collect(Collectors.toList());
    }
}
