package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.CampaignRequest;
import com.mega.warrantymanagementsystem.model.response.CampaignResponse;
import com.mega.warrantymanagementsystem.repository.CampaignRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public CampaignResponse create(CampaignRequest request) {
        boolean exists = campaignRepository.findAll().stream()
                .anyMatch(c -> c.getCampaignName().equalsIgnoreCase(request.getCampaignName()));
        if (exists) {
            throw new DuplicateResourceException("Campaign name đã tồn tại: " + request.getCampaignName());
        }

        Campaign campaign = modelMapper.map(request, Campaign.class);
        Campaign saved = campaignRepository.save(campaign);
        return modelMapper.map(saved, CampaignResponse.class);
    }

    // UPDATE
    public CampaignResponse update(int campaignId, CampaignRequest request) {
        Campaign existing = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Campaign với ID: " + campaignId));

        // kiểm tra trùng tên
        if (!existing.getCampaignName().equalsIgnoreCase(request.getCampaignName())) {
            boolean exists = campaignRepository.findAll().stream()
                    .anyMatch(c -> c.getCampaignName().equalsIgnoreCase(request.getCampaignName()));
            if (exists) {
                throw new DuplicateResourceException("Campaign name đã tồn tại: " + request.getCampaignName());
            }
        }

        existing.setCampaignName(request.getCampaignName());
        existing.setServiceDescription(request.getServiceDescription());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setModel(request.getModel());

        Campaign updated = campaignRepository.save(existing);
        return modelMapper.map(updated, CampaignResponse.class);
    }

    // DELETE
    public void delete(int campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new ResourceNotFoundException("Không tìm thấy Campaign với ID: " + campaignId);
        }
        campaignRepository.deleteById(campaignId);
    }

    // GET ALL
    public List<CampaignResponse> getAll() {
        return campaignRepository.findAll().stream()
                .map(c -> modelMapper.map(c, CampaignResponse.class))
                .collect(Collectors.toList());
    }

    // GET BY ID
    public CampaignResponse getById(int campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Campaign với ID: " + campaignId));
        return modelMapper.map(campaign, CampaignResponse.class);
    }

    // SEARCH BY NAME
    public List<CampaignResponse> searchByName(String name) {
        return campaignRepository.findAll().stream()
                .filter(c -> c.getCampaignName() != null &&
                        c.getCampaignName().toLowerCase().contains(name.toLowerCase()))
                .map(c -> modelMapper.map(c, CampaignResponse.class))
                .collect(Collectors.toList());
    }
}
