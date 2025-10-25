package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WarrantyClaimRepository extends JpaRepository<WarrantyClaim, Integer> {

    // Tìm theo từng thuộc tính riêng
    List<WarrantyClaim> findByServiceCenterStaff_AccountId(String accountId);

    List<WarrantyClaim> findByServiceCenterTechnician_AccountId(String accountId);

    List<WarrantyClaim> findByEvm_AccountId(String accountId);

    List<WarrantyClaim> findByPolicy_PolicyId(int policyId);

    List<WarrantyClaim> findByClaimDate(LocalDate claimDate);

    List<WarrantyClaim> findByStatus(WarrantyClaimStatus status);

    WarrantyClaim findByClaimId(String claimId);

    // Tìm theo VIN (Vehicle)
    List<WarrantyClaim> findByVehicle_Vin(String vin);

}
