package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.ClaimAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimAttachmentRepository extends JpaRepository<ClaimAttachment, Integer> {
    ClaimAttachment findByFilePath(String filePath);
    ClaimAttachment findByFileType(String fileType);
}
