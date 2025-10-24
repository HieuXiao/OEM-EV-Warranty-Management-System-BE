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

    List<VehicleResponse> findByModel(String model);

    void assignCampaignToVehiclesByModel(int campaignId, String model);

    void removeCampaignFromModel(String model);

    void assignCampaignToVehicleByVin(int campaignId, String vin);

    void removeCampaignFromVehicleByVin(String vin);


}
