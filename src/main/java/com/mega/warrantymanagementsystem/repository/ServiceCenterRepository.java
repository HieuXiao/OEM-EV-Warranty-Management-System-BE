package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceCenterRepository extends JpaRepository<ServiceCenter, Integer> {
    ServiceCenter findByCenterName(String name);
    List<ServiceCenter> findByLocation(String location);
    boolean existsByCenterNameIgnoreCase(String centerName);

    boolean existsByLocationIgnoreCase(String location);
}
