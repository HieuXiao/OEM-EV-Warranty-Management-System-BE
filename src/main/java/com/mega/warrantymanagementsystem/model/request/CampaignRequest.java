package com.mega.warrantymanagementsystem.model.request;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CampaignRequest {
    private String campaignName;
    private String serviceDescription;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> model;
}
