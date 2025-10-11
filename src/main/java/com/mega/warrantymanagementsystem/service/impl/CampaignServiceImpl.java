package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.model.request.CampaignRequest;
import com.mega.warrantymanagementsystem.repository.CampaignRepository;
import com.mega.warrantymanagementsystem.service.CampaignService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Campaign createCampaign(CampaignRequest request) {
        if (campaignRepository.findByCampaignName(request.getCampaignName()) != null) {
            throw new DuplicateResourceException("Campaign name already exists!");
        }
        Campaign campaign = modelMapper.map(request, Campaign.class);
        return campaignRepository.save(campaign);
    }

    @Override
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public Campaign getCampaignByName(String name) {
        return campaignRepository.findByCampaignName(name);
    }
}
