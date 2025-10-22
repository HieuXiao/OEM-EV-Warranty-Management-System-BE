package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponse {
    private int campaignId;
    private String campaignName;
    private String serviceDescription;
    private LocalDate startDate;
    private LocalDate endDate;
}
