package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
<<<<<<< HEAD
<<<<<<< HEAD
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
=======
>>>>>>> origin/main
=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
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
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> origin/main
=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
        return campaignRepository.findAll();
    }

    @Override
    public Campaign getCampaignByName(String name) {
<<<<<<< HEAD
<<<<<<< HEAD

        return campaignRepository.findByCampaignName(name);
    }

    @Override
    public Campaign getCampaignById(int id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + id));
        return campaign;
    }

    @Override
    public Campaign updateCampaign(int id, CampaignRequest request) {
        Campaign existing = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + id));

        existing.setCampaignName(request.getCampaignName());
        existing.setServiceDescription(request.getServiceDescription());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());

        return campaignRepository.save(existing);
    }

    @Override
    public void deleteCampaign(int id) {
        Campaign existing = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + id));
        campaignRepository.delete(existing);
    }

=======
        return campaignRepository.findByCampaignName(name);
    }
>>>>>>> origin/main
=======
        return campaignRepository.findByCampaignName(name);
    }
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
}
