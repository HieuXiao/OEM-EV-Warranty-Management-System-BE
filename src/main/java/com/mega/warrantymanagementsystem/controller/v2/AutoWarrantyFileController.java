package com.mega.warrantymanagementsystem.controller.v2;

import com.mega.warrantymanagementsystem.model.response.WarrantyFileResponse;
import com.mega.warrantymanagementsystem.service.v2.AutoWarrantyFileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * API cho phép:
 *  - Upload file và tự động sinh fileId theo logic model_date_claim_index
 *  - Cập nhật ảnh/video (re-upload)
 */
@RestController
@RequestMapping("/api/auto-warranty-files")
@CrossOrigin
@SecurityRequirement(name = "api")
public class AutoWarrantyFileController {

    @Autowired
    private AutoWarrantyFileService autoWarrantyFileService;

    /**
     * Upload ảnh/video lên Cloudinary, tự sinh fileId, lưu WarrantyFile.
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public WarrantyFileResponse uploadAndCreate(
            @RequestParam("claimId") String claimId,
            @RequestPart("files") List<MultipartFile> files) {
        return autoWarrantyFileService.uploadAndCreate(claimId, files);
    }

    /**
     * Update lại ảnh/video của WarrantyFile có sẵn (re-upload).
     */
    @PutMapping(value = "/update/{fileId}", consumes = "multipart/form-data")
    public WarrantyFileResponse updateMedia(
            @PathVariable String fileId,
            @RequestPart("files") List<MultipartFile> files) {
        return autoWarrantyFileService.updateMedia(fileId, files);
    }
}
