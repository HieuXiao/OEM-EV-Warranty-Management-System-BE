package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.response.WarrantyFileResponse;
import com.mega.warrantymanagementsystem.service.CloudinaryService;
import com.mega.warrantymanagementsystem.service.WarrantyFileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@CrossOrigin
@SecurityRequirement(name = "api")
public class MediaController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private WarrantyFileService warrantyFileService;

    /**
     * Upload 1 file (ảnh hoặc video)
     */
    @PostMapping("/upload")
    public String uploadSingle(@RequestParam("file") MultipartFile file) {
        return cloudinaryService.uploadFile(file, "warranty_files");
    }

    /**
     * Upload nhiều file
     */
    @PostMapping("/upload/multi")
    public List<String> uploadMultiple(@RequestParam("files") List<MultipartFile> files) {
        return cloudinaryService.uploadFiles(files, "warranty_files");
    }



}

