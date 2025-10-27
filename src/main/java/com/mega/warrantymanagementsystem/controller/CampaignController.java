package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.model.request.CampaignRequest;
import com.mega.warrantymanagementsystem.model.response.CampaignResponse;
import com.mega.warrantymanagementsystem.model.response.CustomerResponse;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
import com.mega.warrantymanagementsystem.repository.CustomerRepository;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
import com.mega.warrantymanagementsystem.service.CampaignService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin
@SecurityRequirement(name = "api")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // ---------- CREATE ----------
    @PostMapping("/create")
    public CampaignResponse createCampaign(@RequestBody CampaignRequest request) {
        return campaignService.create(request);
    }

    // ---------- UPDATE ----------
    @PutMapping("/update/{id}")
    public CampaignResponse updateCampaign(@PathVariable int id, @RequestBody CampaignRequest request) {
        return campaignService.update(id, request);
    }

    // ---------- DELETE ----------
    @DeleteMapping("/delete/{id}")
    public void deleteCampaign(@PathVariable int id) {
        campaignService.delete(id);
    }

    // ---------- GET ALL ----------
    @GetMapping("/all")
    public List<CampaignResponse> getAllCampaigns() {
        return campaignService.getAll();
    }

    // ---------- GET BY ID ----------
    @GetMapping("/{id}")
    public CampaignResponse getCampaignById(@PathVariable int id) {
        return campaignService.getById(id);
    }

    // ---------- SEARCH ----------
    @GetMapping("/search/by-name")
    public List<CampaignResponse> searchCampaignByName(@RequestParam("value") String name) {
        return campaignService.searchByName(name);
    }

    @GetMapping("/{id}/vehicles")
    public List<VehicleResponse> getVehiclesByCampaignModel(@PathVariable int id) {
        return campaignService.getVehiclesByCampaignModel(id);
    }

    @GetMapping("/{id}/customers")
    public List<CustomerResponse> getCustomersByCampaignModel(@PathVariable int id) {
        return campaignService.getCustomersByCampaignModel(id);
    }

}
