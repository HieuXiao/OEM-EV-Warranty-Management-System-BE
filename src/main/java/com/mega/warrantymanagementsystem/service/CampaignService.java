package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.model.request.CampaignRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CampaignService {
    Campaign createCampaign(CampaignRequest request);
    List<Campaign> getAllCampaigns();
    Campaign getCampaignByName(String name);
}
