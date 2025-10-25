package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CampaignResponse {
    private int campaignId;
    private String campaignName;
    private String serviceDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> model;
}
