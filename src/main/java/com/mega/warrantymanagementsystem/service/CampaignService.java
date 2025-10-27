package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.CampaignRequest;
import com.mega.warrantymanagementsystem.model.response.CampaignResponse;
import com.mega.warrantymanagementsystem.model.response.CustomerResponse;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
import com.mega.warrantymanagementsystem.repository.CampaignRepository;
import com.mega.warrantymanagementsystem.repository.CustomerRepository;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
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
    private VehicleRepository vehicleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    public CampaignResponse create(CampaignRequest request) {
        boolean exists = campaignRepository.existsByCampaignNameIgnoreCase(request.getCampaignName());

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

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

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // kiểm tra trùng nếu tên mới khác tên cũ
        if (!existing.getCampaignName().equalsIgnoreCase(request.getCampaignName())
                && campaignRepository.existsByCampaignNameIgnoreCase(request.getCampaignName())) {
            throw new DuplicateResourceException("Campaign name đã tồn tại: " + request.getCampaignName());
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
        return campaignRepository.findByCampaignNameContainingIgnoreCase(name)
                .stream()
                .map(c -> modelMapper.map(c, CampaignResponse.class))
                .collect(Collectors.toList());
    }

    public List<VehicleResponse> getVehiclesByCampaignModel(int campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy campaign: " + campaignId));

        List<String> models = campaign.getModel();
        return vehicleRepository.findAll().stream()
                .filter(v -> models.contains(v.getModel()))
                .map(v -> modelMapper.map(v, VehicleResponse.class))
                .toList();
    }

    public List<CustomerResponse> getCustomersByCampaignModel(int campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy campaign: " + campaignId));

        List<String> models = campaign.getModel();

        return vehicleRepository.findAll().stream()
                .filter(v -> models.contains(v.getModel()) && v.getCustomer() != null)
                .map(Vehicle::getCustomer)
                .distinct()
                .map(c -> modelMapper.map(c, CustomerResponse.class))
                .toList();
    }

}
