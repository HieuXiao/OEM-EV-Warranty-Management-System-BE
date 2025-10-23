package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.entity.Vehicle;
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

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

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
        vehicle.setPlate(vehicleRequest.getPlate());
        vehicle.setType(vehicleRequest.getType());
        vehicle.setColor(vehicleRequest.getColor());
        vehicle.setModel(vehicleRequest.getModel());
        vehicle.setCustomer(customer);

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
            return new ArrayList<>();
        }

        List<VehicleResponse> responses = new ArrayList<>();
        for (Vehicle v : vehicles) {
            responses.add(modelMapper.map(v, VehicleResponse.class));
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

        for (Vehicle v : vehicles) {
            v.setCampaign(campaign);
        }
        vehicleRepository.saveAll(vehicles);
    }

    @Override
    public void removeCampaignFromModel(String model) {
        List<Vehicle> vehicles = vehicleRepository.findByModel(model);
        for (Vehicle v : vehicles) {
            v.setCampaign(null);
        }
        vehicleRepository.saveAll(vehicles);
    }

    @Override
    public void assignCampaignToVehicleByVin(int campaignId, String vin) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));

        Vehicle vehicle = vehicleRepository.findByVin(vin);
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with VIN: " + vin);
        }

        if (!vehicle.getModel().equalsIgnoreCase(campaign.getModel())) {
            throw new InvalidOperationException("Vehicle model (" + vehicle.getModel() +
                    ") does not match campaign model (" + campaign.getModel() + ")");
        }

        if (vehicle.getCampaign() == null || vehicle.getCampaign().getCampaignId() != campaignId) {
            vehicle.setCampaign(campaign);
            vehicleRepository.save(vehicle);
        }
    }

    @Override
    public void removeCampaignFromVehicleByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findByVin(vin);
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with VIN: " + vin);
        }

        if (vehicle.getCampaign() != null) {
            vehicle.setCampaign(null);
            vehicleRepository.save(vehicle);
        }
    }
}
