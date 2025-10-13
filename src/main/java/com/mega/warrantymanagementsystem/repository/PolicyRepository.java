package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer> {
}
