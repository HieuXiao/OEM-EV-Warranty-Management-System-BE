package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.ClaimPartCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimPartCheckRepository extends JpaRepository<ClaimPartCheck, String> {

    // tìm tất cả claim part check theo vin
    List<ClaimPartCheck> findByVehicle_Vin(String vin);

    // tìm tất cả claim part check theo warrantyId
    List<ClaimPartCheck> findByWarrantyClaim_ClaimId(String warrantyId);

    // kiểm tra xem partNumber có tồn tại chưa
    boolean existsByPartNumber(String partNumber);

    boolean existsByPartNumberAndWarrantyClaim_ClaimId(String partNumber, String claimId);

    ClaimPartCheck findByPartNumberAndWarrantyClaim_ClaimId(String partNumber, String claimId);

    void deleteByWarrantyClaim_ClaimId(String claimId);

}
