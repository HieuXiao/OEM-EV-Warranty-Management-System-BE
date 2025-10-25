package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.VehicleRequest;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý logic cho Vehicle.
 * Bao gồm CRUD và tìm kiếm theo vin, plate, type, color, model.
 */
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Tạo mới Vehicle.
     */
    // Tạo mới Vehicle
    public VehicleResponse create(VehicleRequest request) {
        // Kiểm tra trùng VIN
        if (vehicleRepository.existsById(request.getVin())) {
            throw new DuplicateResourceException("VIN đã tồn tại: " + request.getVin());
        }

        // Kiểm tra trùng plate (đảm bảo không NPE)
        boolean plateExists = vehicleRepository.findAll().stream()
                .anyMatch(v -> v.getPlate() != null && v.getPlate().equalsIgnoreCase(request.getPlate()));
        if (plateExists) {
            throw new DuplicateResourceException("Biển số đã tồn tại: " + request.getPlate());
        }

        Vehicle vehicle = modelMapper.map(request, Vehicle.class);
        Vehicle saved = vehicleRepository.save(vehicle);
        return modelMapper.map(saved, VehicleResponse.class);
    }

    // Cập nhật Vehicle
    public VehicleResponse update(String vin, VehicleRequest request) {
        Vehicle existing = vehicleRepository.findById(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Vehicle với VIN: " + vin));

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

        Vehicle updated = vehicleRepository.save(existing);
        return modelMapper.map(updated, VehicleResponse.class);
    }


    /**
     * Xóa Vehicle theo VIN.
     */
    public void delete(String vin) {
        if (!vehicleRepository.existsById(vin)) {
            throw new ResourceNotFoundException("Không tìm thấy Vehicle với VIN: " + vin);
        }
        vehicleRepository.deleteById(vin);
    }

    /**
     * Lấy tất cả Vehicle.
     */
    public List<VehicleResponse> getAll() {
        return vehicleRepository.findAll().stream()
                .map(v -> modelMapper.map(v, VehicleResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy Vehicle theo VIN.
     */
    public VehicleResponse getByVin(String vin) {
        Vehicle vehicle = vehicleRepository.findById(vin)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Vehicle với VIN: " + vin));
        return modelMapper.map(vehicle, VehicleResponse.class);
    }

    /**
     * Tìm theo biển số (plate).
     */
    public List<VehicleResponse> getByPlate(String plate) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getPlate() != null && v.getPlate().toLowerCase().contains(plate.toLowerCase()))
                .map(v -> modelMapper.map(v, VehicleResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo loại xe (type).
     */
    public List<VehicleResponse> getByType(String type) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getType() != null && v.getType().toLowerCase().contains(type.toLowerCase()))
                .map(v -> modelMapper.map(v, VehicleResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo màu (color).
     */
    public List<VehicleResponse> getByColor(String color) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getColor() != null && v.getColor().toLowerCase().contains(color.toLowerCase()))
                .map(v -> modelMapper.map(v, VehicleResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo model xe.
     */
    public List<VehicleResponse> getByModel(String model) {
        return vehicleRepository.findAll().stream()
                .filter(v -> v.getModel() != null && v.getModel().toLowerCase().contains(model.toLowerCase()))
                .map(v -> modelMapper.map(v, VehicleResponse.class))
                .collect(Collectors.toList());
    }
}
