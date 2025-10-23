package com.mega.warrantymanagementsystem.model.response;

import com.mega.warrantymanagementsystem.entity.ClaimAttachment;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimAttachmentResponse {
    private int attachmentId;
    private String fileType;
    private String filePath;

    private Integer claimId;

}
