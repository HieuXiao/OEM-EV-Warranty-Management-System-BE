package com.mega.warrantymanagementsystem.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, String folder) {
        try {
            Map uploadParams = ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "auto" // Cho phép ảnh, video, pdf, v.v.
            );
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload to Cloudinary failed: " + e.getMessage());
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files, String folder) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadFile(file, folder));
        }
        return urls;
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map uploadFileWithResult(MultipartFile file, String folder) {
        try {
            String originalName = Optional.ofNullable(file.getOriginalFilename())
                    .orElse("unnamed.pdf");
            String cleanName = originalName.replaceAll("\\s+", "_"); // bỏ khoảng trắng

            boolean isPdf = cleanName.toLowerCase().endsWith(".pdf");
            Map uploadParams = ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", isPdf ? "raw" : "auto",
                    "public_id", cleanName  // quan trọng: giữ tên file gốc
            );

            return cloudinary.uploader().upload(file.getBytes(), uploadParams);
        } catch (IOException e) {
            throw new RuntimeException("Upload to Cloudinary failed: " + e.getMessage());
        }
    }


    private void validatePdfFiles(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            String ct = file.getContentType();
            String name = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
            boolean ok = "application/pdf".equals(ct) || "application/octet-stream".equals(ct) || name.endsWith(".pdf");
            if (!ok) {
                throw new BusinessLogicException("Chỉ được phép upload file PDF, file '" + file.getOriginalFilename() + "' không hợp lệ.");
            }
        }
    }


}
