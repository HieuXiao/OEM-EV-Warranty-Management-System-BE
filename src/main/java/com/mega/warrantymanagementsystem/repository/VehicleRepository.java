package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByCustomer_CustomerPhone(String customerPhone);
    Vehicle findByVin(String vin);
    List<Vehicle> findByModel(String model);
    List<Vehicle> findByPlate(String plate);
    List<Vehicle> findByColor(String color);
    List<Vehicle> findByCampaign_CampaignName(String campaignName);

}
