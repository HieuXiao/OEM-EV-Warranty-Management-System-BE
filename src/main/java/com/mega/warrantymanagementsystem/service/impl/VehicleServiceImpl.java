package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.InvalidOperationException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.VehicleRequest;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
import com.mega.warrantymanagementsystem.repository.CampaignRepository;
import com.mega.warrantymanagementsystem.repository.CustomerRepository;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
import com.mega.warrantymanagementsystem.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    //model mapper để chuyển đổi giữa entity và DTO
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public VehicleResponse findByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findByVin(vin);
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with VIN: " + vin);
        }
        return modelMapper.map(vehicle, VehicleResponse.class);
    }

    @Override
    public List<VehicleResponse> findByCustomerPhone(String customerPhone) {
        List<Vehicle> vehicles = vehicleRepository.findByCustomer_CustomerPhone(customerPhone);
        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No vehicles found for customerPhone: " + customerPhone);
        }

        List<VehicleResponse> responses = new ArrayList<>();
        for (Vehicle v : vehicles) {
            responses.add(modelMapper.map(v, VehicleResponse.class));
        }
        return responses;
    }

    @Override
    public VehicleResponse createVehicle(VehicleRequest vehicleRequest) {
        Customer customer = customerRepository.findByCustomerPhone(vehicleRequest.getCustomerPhone());
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with phone: " + vehicleRequest.getCustomerPhone());
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vehicleRequest.getVin());
        vehicle.setYear(vehicleRequest.getYear());
        vehicle.setColor(vehicleRequest.getColor());
        vehicle.setModel(vehicleRequest.getModel());
        vehicle.setCustomer(customer);
        // Không set campaign ở đây nữa

        Vehicle saved = vehicleRepository.save(vehicle);
        return modelMapper.map(saved, VehicleResponse.class);
    }

    @Override
    public List<VehicleResponse> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleResponse> responses = new ArrayList<>();
        for (Vehicle v : vehicles) {
            responses.add(modelMapper.map(v, VehicleResponse.class));
        }
        return responses;
    }

    @Override
    public List<VehicleResponse> findByModel(String model) {
        List<Vehicle> vehicles = vehicleRepository.findByModel(model);

        if (vehicles == null || vehicles.isEmpty()) {
            return new ArrayList<VehicleResponse>();
        }

        List<VehicleResponse> responses = new ArrayList<VehicleResponse>();
        for (Vehicle vehicle : vehicles) {
            VehicleResponse vr = modelMapper.map(vehicle, VehicleResponse.class);
            responses.add(vr);
        }
        return responses;
    }

    @Override
    public void assignCampaignToVehiclesByModel(int campaignId, String model) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        List<Vehicle> vehicles = vehicleRepository.findByModel(model);
        if (vehicles == null || vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No vehicles found for model: " + model);
        }

        List<Vehicle> toSave = new ArrayList<Vehicle>();
        for (Vehicle vehicle : vehicles) {
            vehicle.setCampaign(campaign);
            toSave.add(vehicle);
        }

        vehicleRepository.saveAll(toSave);
    }

    @Override
    public void removeCampaignFromModel(String model) {
        List<Vehicle> vehicles = vehicleRepository.findByModel(model);
        for (Vehicle v : vehicles) {
            v.setCampaign(null);
            vehicleRepository.save(v);
        }
    }

    @Override
    public void assignCampaignToVehicleByVin(int campaignId, String vin) {
        // Tìm campaign theo ID
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        // Tìm vehicle theo VIN
        Vehicle vehicle = vehicleRepository.findByVin(vin);
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with VIN: " + vin);
        }

        // Kiểm tra model của vehicle có trùng với model mà campaign áp dụng không
        if (!vehicle.getModel().equalsIgnoreCase(campaign.getModel())) {
            // Nếu khác thì báo lỗi, không cho phép gán
            throw new InvalidOperationException(
                    "Vehicle model (" + vehicle.getModel() +
                            ") does not match campaign model (" + campaign.getModel() + ")");
        }

        // Nếu model hợp lệ và campaign chưa được gán, thì gán vào vehicle
        if (vehicle.getCampaign() == null || vehicle.getCampaign().getCampaignId() != campaignId) {
            vehicle.setCampaign(campaign);
            vehicleRepository.save(vehicle);
        }
    }


    @Override
    public void removeCampaignFromVehicleByVin(String vin) {
        // Tìm vehicle
        Vehicle vehicle = vehicleRepository.findByVin(vin);
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with VIN: " + vin);
        }

        // Gỡ campaign ra nếu đang được gán
        if (vehicle.getCampaign() != null) {
            vehicle.setCampaign(null);
            vehicleRepository.save(vehicle);
        }
    }


}
