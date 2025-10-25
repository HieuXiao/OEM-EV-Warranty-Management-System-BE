package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.WarrantyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyFileRepository extends JpaRepository<WarrantyFile, String> {
    List<WarrantyFile> findByWarrantyClaim_ClaimId(String claimId);

}
