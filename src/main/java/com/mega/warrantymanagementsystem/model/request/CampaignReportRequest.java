package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignReportRequest {

    @NotNull(message = "Campaign ID is required")
    private Integer campaignId;

    @NotNull(message = "Service Center ID is required")
    private Integer serviceCenterId;

    @NotEmpty(message = "At least one submitter is required")
    private List<String> submittedByIds; // danh sách accountId của người nộp báo cáo

    private List<String> reportFileUrls; // danh sách URL các file PDF đã upload (Cloudinary, S3,...)

    private String originalFileName; // tên gốc file khi upload

    private LocalDateTime submittedAt; // có thể null -> service sẽ tự set LocalDateTime.now()

}
