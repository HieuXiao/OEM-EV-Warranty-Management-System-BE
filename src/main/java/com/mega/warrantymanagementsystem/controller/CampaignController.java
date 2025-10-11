package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.CampaignRequest;
import com.mega.warrantymanagementsystem.service.CampaignService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign")
@CrossOrigin
@SecurityRequirement(name = "api")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @PostMapping("/create")
    public ResponseEntity<Campaign> createCampaign(@RequestBody CampaignRequest request) {
        Campaign created = campaignService.createCampaign(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/")
    public ResponseEntity<List<Campaign>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    @GetMapping("/search")
    public ResponseEntity<Campaign> getCampaignByName(@RequestParam("name") String name) {
        Campaign campaign = campaignService.getCampaignByName(name);
        if (campaign == null) {
            throw new ResourceNotFoundException("Campaign not found with name: " + name);
        }
        return ResponseEntity.ok(campaign);
    }
}
