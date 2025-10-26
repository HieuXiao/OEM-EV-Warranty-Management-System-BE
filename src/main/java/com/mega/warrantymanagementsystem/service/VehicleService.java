package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.VehicleRequest;
import com.mega.warrantymanagementsystem.model.response.CampaignResponse;
import com.mega.warrantymanagementsystem.model.response.CustomerResponse;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
import com.mega.warrantymanagementsystem.repository.CampaignRepository;
import com.mega.warrantymanagementsystem.repository.CustomerRepository;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service xử lý logic cho Vehicle:
 * - CRUD đầy đủ
 * - Cho phép gắn Campaign và Customer thủ công bằng ID
 * - Map trả về CampaignResponse, CustomerResponse đầy đủ
 */
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * -------------------- CREATE --------------------
     */
    public VehicleResponse create(VehicleRequest request) {
        // Kiểm tra VIN trùng
        if (vehicleRepository.existsById(request.getVin())) {
            throw new DuplicateResourceException("VIN đã tồn tại: " + request.getVin());
        }

        // Kiểm tra biển số trùng
        boolean plateExists = vehicleRepository.findAll().stream()
                .anyMatch(v -> v.getPlate() != null && v.getPlate().equalsIgnoreCase(request.getPlate()));
        if (plateExists) {
            throw new DuplicateResourceException("Biển số đã tồn tại: " + request.getPlate());
        }

        Vehicle vehicle = modelMapper.map(request, Vehicle.class);

        // Gắn Customer nếu có
        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Customer với ID: " + request.getCustomerId()));
            vehicle.setCustomer(customer);
        }

        // Gắn Campaign nếu có
        if (request.getCampaignId() != null) {
            Campaign campaign = campaignRepository.findById(request.getCampaignId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Campaign với ID: " + request.getCampaignId()));
            vehicle.setCampaign(campaign);
        }

        Vehicle saved = vehicleRepository.save(vehicle);
        return mapToResponse(saved);
    }

    /**
     * -------------------- UPDATE --------------------
     */
    public VehicleResponse update(String vin, VehicleRequest request) {
        Vehicle existing = vehicleRepository.findById(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Vehicle với VIN: " + vin));

        // Kiểm tra biển số có bị trùng không
        if (request.getPlate() != null &&
                !request.getPlate().equalsIgnoreCase(existing.getPlate())) {

            boolean plateExists = vehicleRepository.findAll().stream()
                    .anyMatch(v -> v.getPlate() != null && v.getPlate().equalsIgnoreCase(request.getPlate()));
            if (plateExists) {
                throw new DuplicateResourceException("Biển số đã tồn tại: " + request.getPlate());
            }
        }

        existing.setPlate(request.getPlate());
        existing.setType(request.getType());
        existing.setColor(request.getColor());
        existing.setModel(request.getModel());

        // Gắn lại Customer nếu có
        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Customer với ID: " + request.getCustomerId()));
            existing.setCustomer(customer);
        }

        // Gắn lại Campaign nếu có
        if (request.getCampaignId() != null) {
            Campaign campaign = campaignRepository.findById(request.getCampaignId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Campaign với ID: " + request.getCampaignId()));
            existing.setCampaign(campaign);
        }

        Vehicle updated = vehicleRepository.save(existing);
        return mapToResponse(updated);
    }

    /**
     * -------------------- DELETE --------------------
     */
    public void delete(String vin) {
        if (!vehicleRepository.existsById(vin)) {
            throw new ResourceNotFoundException("Không tìm thấy Vehicle với VIN: " + vin);
        }
        vehicleRepository.deleteById(vin);
    }

    /**
     * -------------------- GET ALL --------------------
     */
    public List<VehicleResponse> getAll() {
        return vehicleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * -------------------- GET BY VIN --------------------
     */
    public VehicleResponse getByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findById(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Vehicle với VIN: " + vin));
        return mapToResponse(vehicle);
    }

    /**
     * -------------------- FILTER --------------------
     */
    public List<VehicleResponse> getByPlate(String plate) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getPlate() != null && v.getPlate().equalsIgnoreCase(plate))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VehicleResponse> getByType(String type) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getType() != null && v.getType().equalsIgnoreCase(type))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VehicleResponse> getByColor(String color) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getColor() != null && v.getColor().equalsIgnoreCase(color))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VehicleResponse> getByModel(String model) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getModel() != null && v.getModel().equalsIgnoreCase(model))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * -------------------- MAP ENTITY → RESPONSE --------------------
     */
    private VehicleResponse mapToResponse(Vehicle vehicle) {
        VehicleResponse response = modelMapper.map(vehicle, VehicleResponse.class);

        if (vehicle.getCustomer() != null) {
            response.setCustomer(modelMapper.map(vehicle.getCustomer(), CustomerResponse.class));
        }

        if (vehicle.getCampaign() != null) {
            response.setCampaign(modelMapper.map(vehicle.getCampaign(), CampaignResponse.class));
        }

        return response;
    }

    /**
     * Lấy danh sách Vehicle theo customerId.
     */
    public List<VehicleResponse> getByCustomerId(int customerId) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getCustomer() != null
                        && v.getCustomer().getCustomerId() == customerId)
                .map(v -> modelMapper.map(v, VehicleResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * xóa campaign khỏi vehicle
     * @param vin vin của vehicle
     * @param campaignId id của campaign muốn xóa
     * @return thông báo thành công hoặc lỗi
     */
    public String removeCampaignFromVehicle(String vin, int campaignId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vin);
        if (vehicleOpt.isEmpty()) {
            return "vehicle not found";
        }
        Vehicle vehicle = vehicleOpt.get();

        Campaign campaign = vehicle.getCampaign();
        if (campaign == null || campaign.getCampaignId() != campaignId) {
            return "vehicle is not associated with this campaign";
        }

        // xóa campaign
        vehicle.setCampaign(null);
        vehicleRepository.save(vehicle);
        return "campaign removed from vehicle successfully";
    }

    /**
     * gắn campaign vào vehicle
     * @param vin vin của vehicle
     * @param campaignId id của campaign muốn gắn
     * @return thông báo thành công hoặc lỗi
     */
    public String assignCampaignToVehicle(String vin, int campaignId) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findById(vin);
        if (vehicleOpt.isEmpty()) {
            return "vehicle not found";
        }
        Vehicle vehicle = vehicleOpt.get();

        Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
        if (campaignOpt.isEmpty()) {
            return "campaign not found";
        }
        Campaign campaign = campaignOpt.get();

        // kiểm tra model trùng
        if(campaign.getModel() == null ||
                campaign.getModel().stream().noneMatch(m -> m.equalsIgnoreCase(vehicle.getModel()))) {
            throw new RuntimeException("vehicle model does not match campaign model");
        }


        // gắn campaign vào vehicle
        vehicle.setCampaign(campaign);
        vehicleRepository.save(vehicle);
        return "campaign assigned to vehicle successfully";
    }

}
