package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
<<<<<<< HEAD
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> origin/main
=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByCustomer_CustomerPhone(String customerPhone);
<<<<<<< HEAD
<<<<<<< HEAD
    Vehicle findByVin(String vin);
    List<Vehicle> findByModel(String model);
=======
    Vehicle findByVin(String Vin);
>>>>>>> origin/main
=======
    Vehicle findByVin(String Vin);
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c

}
