package com.mega.warrantymanagementsystem.service.v2;

import com.mega.warrantymanagementsystem.entity.ClaimPartCheck;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.entity.WarrantyFile;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.response.WarrantyFileResponse;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyFileRepository;
import com.mega.warrantymanagementsystem.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Upload ảnh WarrantyFile:
 *  - Tự động sinh fileId theo: model_date_claim_index
 *  - Chỉ cho phép upload khi claim có ít nhất 1 part cần sửa (isRepair=true)
 *  - Nếu claim đang CHECK và có ảnh => chuyển sang DECIDE
 */
@Service
public class AutoWarrantyFileService {

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private WarrantyFileRepository warrantyFileRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Upload file lên Cloudinary và tạo WarrantyFile với fileId tự động.
     * Kiểm tra logic claim & part trước khi cho phép.
     */
    @Transactional
    public WarrantyFileResponse uploadAndCreate(String claimId, List<MultipartFile> files) {
        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim: " + claimId));

        // Ép Hibernate load claim thật (tránh proxy lỗi)
        warrantyClaimRepository.flush();

        boolean hasRepairPart = claim.getClaimPartChecks() != null &&
                claim.getClaimPartChecks().stream()
                        .anyMatch(c -> Boolean.TRUE.equals(c.getIsRepair()));

        if (!hasRepairPart) {
            throw new BusinessLogicException("Không có bộ phận cần sửa nên không cần ảnh.");
        }

        List<String> urls = cloudinaryService.uploadFiles(files, "warranty_files");

        String fileId = generateFileId(claim);

        WarrantyFile file = new WarrantyFile();
        file.setFileId(fileId);
        file.setWarrantyClaim(claim); // claim thật, không proxy
        file.setMediaUrls(urls);

        WarrantyFile saved = warrantyFileRepository.saveAndFlush(file);

        if (claim.getStatus() == WarrantyClaimStatus.CHECK) {
            claim.setStatus(WarrantyClaimStatus.DECIDE);
            warrantyClaimRepository.saveAndFlush(claim);
        }

        return toResponse(saved);
    }


    /**
     * Cập nhật lại mediaUrls (update ảnh/video cho WarrantyFile có sẵn).
     */
    @Transactional
    public WarrantyFileResponse updateMedia(String fileId, List<MultipartFile> files) {
        WarrantyFile file = warrantyFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyFile: " + fileId));

        // Upload lại các file mới lên Cloudinary
        List<String> urls = cloudinaryService.uploadFiles(files, "warranty_files");
        file.setMediaUrls(urls);

        WarrantyFile updated = warrantyFileRepository.save(file);
        return toResponse(updated);
    }

    /**
     * Sinh fileId theo công thức: model_date_claimId_index
     * Ví dụ: VF9_20251026_WDC2020_1
     */
    private String generateFileId(WarrantyClaim claim) {
        Vehicle vehicle = claim.getVehicle();
        if (vehicle == null) {
            throw new IllegalStateException("Claim không có thông tin vehicle để tạo fileId.");
        }

        String model = vehicle.getModel().replaceAll("\\s+", "").toUpperCase();
        String date = claim.getClaimDate().format(DateTimeFormatter.BASIC_ISO_DATE);
        String claimId = claim.getClaimId();

        // Lấy tất cả file cùng claim để đếm số thứ tự
        List<WarrantyFile> existingFiles = warrantyFileRepository.findAll().stream()
                .filter(f -> f.getWarrantyClaim() != null &&
                        f.getWarrantyClaim().getClaimId().equals(claimId))
                .collect(Collectors.toList());

        int index = existingFiles.size() + 1;
        return String.format("%s_%s_%s_%d", model, date, claimId, index);
    }

    private WarrantyFileResponse toResponse(WarrantyFile file) {
        WarrantyFileResponse res = new WarrantyFileResponse();
        res.setFileId(file.getFileId());
        res.setClaimId(file.getWarrantyClaim() != null ? file.getWarrantyClaim().getClaimId() : null);
        res.setMediaUrls(file.getMediaUrls());
        return res;
    }
}
