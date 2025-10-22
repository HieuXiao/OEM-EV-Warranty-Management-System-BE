package com.mega.warrantymanagementsystem.model.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimAttachmentRequest {

    private int attachmentId;

    @NotEmpty(message = "file_type cannot be empty")
    private String fileType;

    @NotEmpty(message = "file_path cannot be empty")
    private String filePath;

    // Thêm claimId để map FK: client sẽ gửi claimId khi attach file vào claim đã có
    private Integer claimId;
}
