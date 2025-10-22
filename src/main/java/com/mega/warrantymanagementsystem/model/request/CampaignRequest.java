package com.mega.warrantymanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRequest {
    private String campaignName;
    private String serviceDescription;
    private LocalDate startDate;
    private LocalDate endDate;

    // Thêm model cho request
    private String model;
}
