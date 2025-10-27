package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    Policy findByPolicyName(String policyName);
    List<Policy> findByIsEnable(Boolean isEnable);
    List<Policy> findByPartUnderWarranty_PartId(String partId);

}
