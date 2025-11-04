package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignReportResponse {

    private Integer reportId;

    private List<String> reportFileUrls;

    private String originalFileName;

    private LocalDateTime submittedAt;

    private Integer campaignId;

    private Integer serviceCenterId;

    private List<String> submittedByIds;

}
