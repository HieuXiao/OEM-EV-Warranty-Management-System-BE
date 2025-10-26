package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
    Campaign findByCampaignName(String campaignName);
    boolean existsByCampaignNameIgnoreCase(String campaignName);
    List<Campaign> findByCampaignNameContainingIgnoreCase(String campaignName);


}
