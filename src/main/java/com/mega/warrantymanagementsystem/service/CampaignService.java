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
<<<<<<< HEAD


    Campaign getCampaignById(int id);

    Campaign updateCampaign(int id, CampaignRequest request);

    void deleteCampaign(int id);

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
}
