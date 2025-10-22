package com.mega.warrantymanagementsystem.service.impl;

<<<<<<< HEAD

import com.mega.warrantymanagementsystem.entity.Campaign;

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.VehicleRequest;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
<<<<<<< HEAD

import com.mega.warrantymanagementsystem.repository.CampaignRepository;

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
import com.mega.warrantymanagementsystem.repository.CustomerRepository;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
import com.mega.warrantymanagementsystem.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<<<<<<< HEAD

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

<<<<<<< HEAD

    //model mapper để chuyển đổi giữa entity và DTO

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

<<<<<<< HEAD

    @Autowired
    private CampaignRepository campaignRepository;


=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
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
<<<<<<< HEAD

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
        if (vehicleRepository.findByVin(vehicleRequest.getVin()) != null) {
            throw new DuplicateResourceException("Vehicle with VIN already exists!");
        }

        Customer customer = customerRepository.findByCustomerPhone(vehicleRequest.getCustomerPhone());
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with phone: " + vehicleRequest.getCustomerPhone());
        }

<<<<<<< HEAD

        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vehicleRequest.getVin());
        vehicle.setYear(vehicleRequest.getYear());
        vehicle.setColor(vehicleRequest.getColor());
        vehicle.setModel(vehicleRequest.getModel());
        vehicle.setCustomer(customer);
        // Không set campaign ở đây nữa

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
        Vehicle vehicle = modelMapper.map(vehicleRequest, Vehicle.class);
        vehicle.setCustomer(customer);

        int currentYear = LocalDate.now().getYear();
        int vehicleYear = vehicleRequest.getYear();
        if(vehicleYear > currentYear){
            throw new IllegalArgumentException("Vehicle year must be less than current year: " + currentYear);
        }

<<<<<<< HEAD

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
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

<<<<<<< HEAD

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
=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
}
