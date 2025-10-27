package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

/**
 * Request cho API tự động tạo WarrantyFile.
 * Không cần nhập fileId – hệ thống sẽ tự sinh dựa vào model, ngày và claimId.
 */
@Data
public class AutoWarrantyFileRequest {

    @NotEmpty(message = "Claim ID cannot be empty")
    private String claimId;

    private List<String> mediaUrls; // Danh sách link ảnh/video (Cloudinary URLs)
}
