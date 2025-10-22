package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> origin/main

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByCustomer_CustomerPhone(String customerPhone);
<<<<<<< HEAD
    Vehicle findByVin(String vin);
    List<Vehicle> findByModel(String model);
=======
    Vehicle findByVin(String Vin);
>>>>>>> origin/main

}
