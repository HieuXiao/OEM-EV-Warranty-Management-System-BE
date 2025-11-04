package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.CampaignReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignReportRepository extends JpaRepository<CampaignReport, Integer> {

    // Tìm tất cả report theo campaign
    List<CampaignReport> findByCampaign_CampaignId(Integer campaignId);

    // Tìm tất cả report theo service center
    List<CampaignReport> findByServiceCenter_CenterId(Integer centerId);

    // Tìm report theo account nộp (submittedBy)
    List<CampaignReport> findBySubmittedBy_AccountId(String accountId);

}
