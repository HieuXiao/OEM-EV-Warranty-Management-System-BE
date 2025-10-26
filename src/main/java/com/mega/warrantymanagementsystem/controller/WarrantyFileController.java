package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.WarrantyFileRequest;
import com.mega.warrantymanagementsystem.model.response.WarrantyFileResponse;
import com.mega.warrantymanagementsystem.service.WarrantyFileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller quản lý WarrantyFile.
 * Cung cấp các API CRUD và search theo fileId, claimId, all.
 */
@RestController
@RequestMapping("/api/warranty-files")
@CrossOrigin
@SecurityRequirement(name = "api")
public class WarrantyFileController {

    @Autowired
    private WarrantyFileService warrantyFileService;

    /**
     * Tạo mới WarrantyFile.
     */
    @PostMapping
    public WarrantyFileResponse create(@RequestBody WarrantyFileRequest request) {
        return warrantyFileService.create(request);
    }

    /**
     * Cập nhật WarrantyFile theo ID.
     */
    @PutMapping("/{fileId}")
    public WarrantyFileResponse update(@PathVariable String fileId,
                                       @RequestBody WarrantyFileRequest request) {
        return warrantyFileService.update(fileId, request);
    }

    /**
     * Xóa WarrantyFile theo ID.
     */
    @DeleteMapping("/{fileId}")
    public void delete(@PathVariable String fileId) {
        warrantyFileService.delete(fileId);
    }

    /**
     * Lấy tất cả WarrantyFile.
     */
    @GetMapping
    public List<WarrantyFileResponse> getAll() {
        return warrantyFileService.getAll();
    }

    /**
     * Lấy WarrantyFile theo ID.
     */
    @GetMapping("/{fileId}")
    public WarrantyFileResponse getById(@PathVariable String fileId) {
        return warrantyFileService.getById(fileId);
    }

    /**
     * Lấy danh sách WarrantyFile theo claimId.
     * /api/warranty-files/search/claim?value=CLAIM123
     */
    @GetMapping("/search/claim")
    public List<WarrantyFileResponse> getByClaimId(@RequestParam("value") String claimId) {
        return warrantyFileService.getByClaimId(claimId);
    }

    @PostMapping("/upload/save")
    public WarrantyFileResponse uploadAndSave(
            @RequestParam("fileId") String fileId,
            @RequestParam("claimId") String claimId,
            @RequestParam("files") List<MultipartFile> files) {

        return warrantyFileService.uploadAndSave(fileId, claimId, files);
    }
}
