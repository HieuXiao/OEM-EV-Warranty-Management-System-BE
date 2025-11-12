package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.WarrantyClaimProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyClaimProgressRepository extends JpaRepository<WarrantyClaimProgress, Long> {
    List<WarrantyClaimProgress> findByWarrantyClaim_ClaimIdOrderByTimestampAsc(String claimId);
}
