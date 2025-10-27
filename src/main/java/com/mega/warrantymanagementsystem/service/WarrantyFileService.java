package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.ClaimPartCheck;
import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.entity.WarrantyFile;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarrantyFileRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyFileResponse;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WarrantyFileService {

    @Autowired
    private WarrantyFileRepository warrantyFileRepository;

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    // --- Tạo mới ---
    public WarrantyFileResponse create(WarrantyFileRequest request) {
        WarrantyClaim claim = warrantyClaimRepository.findById(request.getClaimId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim với ID: " + request.getClaimId()));

        WarrantyFile file = new WarrantyFile();
        // nếu client không gửi fileId, gen UUID ngắn
        file.setFileId(request.getFileId() == null || request.getFileId().isBlank()
                ? "WF-" + UUID.randomUUID().toString()
                : request.getFileId());
        file.setWarrantyClaim(claim);
        file.setMediaUrls(request.getMediaUrls());

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

        existing.setMediaUrls(request.getMediaUrls());
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
        res.setMediaUrls(file.getMediaUrls());
        res.setClaimId(file.getWarrantyClaim() != null ? file.getWarrantyClaim().getClaimId() : null);
        return res;
    }

    // --- Upload trực tiếp ---
    public WarrantyFileResponse uploadAndSave(String fileId, String claimId, List<MultipartFile> files) {
        List<String> urls = cloudinaryService.uploadFiles(files, "warranty_files");

        WarrantyFileRequest req = new WarrantyFileRequest();
        req.setFileId(fileId);
        req.setClaimId(claimId);
        req.setMediaUrls(urls);

        return create(req);
    }

    // --- Tạo file từ danh sách URL (ảnh đã có) ---
    public WarrantyFileResponse createFromUrls(String fileId, String claimId, List<String> urls) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim với ID: " + claimId));

        // CHỈ: nếu có ít nhất 1 ClaimPartCheck với isRepair == true
        boolean anyRepair = claim.getClaimPartChecks() != null &&
                claim.getClaimPartChecks()
                        .stream()
                        // không dùng method reference -> dùng getIsRepair() và so sánh Boolean.TRUE để null-safe
                        .anyMatch(c -> Boolean.TRUE.equals(c.getIsRepair()));

        if (!anyRepair) {
            throw new BusinessLogicException("Không có bộ phận nào được đánh dấu cần sửa (isRepair=true). Không thể thêm ảnh.");
        }

        WarrantyFileRequest req = new WarrantyFileRequest();
        req.setFileId(fileId);
        req.setClaimId(claimId);
        req.setMediaUrls(urls);

        WarrantyFileResponse response = create(req);

        // Nếu claim đang ở CHECK -> chuyển sang DECIDE
        if (claim.getStatus() == WarrantyClaimStatus.CHECK) {
            claim.setStatus(WarrantyClaimStatus.DECIDE);
            warrantyClaimRepository.save(claim);
        }

        return response;
    }
}
