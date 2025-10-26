package com.mega.warrantymanagementsystem.controller.v2;

import com.mega.warrantymanagementsystem.model.response.WarrantyFileResponse;
import com.mega.warrantymanagementsystem.service.CloudinaryService;
import com.mega.warrantymanagementsystem.service.WarrantyFileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller hợp nhất giữa MediaController và WarrantyFileController.
 * Cho phép upload file lên Cloudinary và lưu WarrantyFile trong DB cùng lúc.
 */
@RestController
@RequestMapping("/api/warranty-files/combined")
@CrossOrigin
@SecurityRequirement(name = "api")
public class WarrantyFileCombinedController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private WarrantyFileService warrantyFileService;

    /**
     * Upload file(s) lên Cloudinary và tạo WarrantyFile luôn.
     *
     * Form-data gửi:
     *  - fileId: string
     *  - claimId: string
     *  - files: danh sách file ảnh/video
     */
    @PostMapping(value = "/upload-create", consumes = "multipart/form-data")
    public WarrantyFileResponse uploadAndCreate(
            @RequestParam("fileId") String fileId,
            @RequestParam("claimId") String claimId,
            @RequestPart("files") List<MultipartFile> files) {

        List<String> urls = cloudinaryService.uploadFiles(files, "warranty_files");
        return warrantyFileService.createFromUrls(fileId, claimId, urls);
    }

}
