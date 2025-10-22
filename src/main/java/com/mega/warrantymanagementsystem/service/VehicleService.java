package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.model.request.VehicleRequest;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;

import java.util.List;

public interface VehicleService {

    VehicleResponse findByVin(String vin);

    List<VehicleResponse> findByCustomerPhone(String customerPhone);

    VehicleResponse createVehicle(VehicleRequest vehicleRequest);

    List<VehicleResponse> getAllVehicles();


<<<<<<< HEAD
    List<VehicleResponse> findByModel(String model);

    void assignCampaignToVehiclesByModel(int campaignId, String model);

    void removeCampaignFromModel(String model);


=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
}
