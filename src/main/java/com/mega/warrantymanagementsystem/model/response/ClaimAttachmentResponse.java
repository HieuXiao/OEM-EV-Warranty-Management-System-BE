package com.mega.warrantymanagementsystem.model.response;

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
