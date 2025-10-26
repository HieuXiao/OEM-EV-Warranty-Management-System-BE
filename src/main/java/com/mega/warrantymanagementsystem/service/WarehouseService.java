package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Warehouse;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarehouseRequest;
import com.mega.warrantymanagementsystem.model.response.WarehouseResponse;
import com.mega.warrantymanagementsystem.repository.WarehouseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý logic cho Warehouse.
 * Bao gồm CRUD và tìm kiếm theo name, location (so sánh chính xác, không phân biệt hoa thường).
 */
@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public WarehouseResponse create(WarehouseRequest request) {
        boolean exists = warehouseRepository.findAll().stream()
                .anyMatch(w -> (w.getName() != null && w.getName().equalsIgnoreCase(request.getName()))
                        || (w.getLocation() != null && w.getLocation().equalsIgnoreCase(request.getLocation())));

        if (exists) {
            throw new DuplicateResourceException("Warehouse name or location already exists");
        }

        Warehouse warehouse = modelMapper.map(request, Warehouse.class);
        Warehouse saved = warehouseRepository.save(warehouse);
        return modelMapper.map(saved, WarehouseResponse.class);
    }

    public WarehouseResponse update(Integer id, WarehouseRequest request) {
        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse không tồn tại với ID: " + id));

        boolean exists = warehouseRepository.findAll().stream()
                .anyMatch(w -> w.getWhId() != id &&
                        ((w.getName() != null && w.getName().equalsIgnoreCase(request.getName())) ||
                                (w.getLocation() != null && w.getLocation().equalsIgnoreCase(request.getLocation()))));

        if (exists) {
            throw new DuplicateResourceException("Warehouse name or location already exists");
        }

        existing.setName(request.getName());
        existing.setLocation(request.getLocation());

        Warehouse updated = warehouseRepository.save(existing);
        return modelMapper.map(updated, WarehouseResponse.class);
    }

    public void delete(Integer id) {
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse does not exist: " + id);
        }
        warehouseRepository.deleteById(id);
    }

    public List<WarehouseResponse> getAll() {
        return warehouseRepository.findAll().stream()
                .map(w -> modelMapper.map(w, WarehouseResponse.class))
                .collect(Collectors.toList());
    }

    public WarehouseResponse getById(Integer id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse không tồn tại với ID: " + id));
        return modelMapper.map(warehouse, WarehouseResponse.class);
    }

    /**
     * Tìm kiếm theo tên kho (so sánh chính xác, không phân biệt hoa thường).
     */
    public List<WarehouseResponse> getByName(String name) {
        String normalized = name.trim().toLowerCase();
        return warehouseRepository.findAll().stream()
                .filter(w -> w.getName() != null &&
                        w.getName().trim().toLowerCase().equals(normalized))
                .map(w -> modelMapper.map(w, WarehouseResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Tìm kiếm theo vị trí kho (so sánh chính xác, không phân biệt hoa thường).
     */
    public List<WarehouseResponse> getByLocation(String location) {
        String normalized = location.trim().toLowerCase();
        return warehouseRepository.findAll().stream()
                .filter(w -> w.getLocation() != null &&
                        w.getLocation().trim().toLowerCase().equals(normalized))
                .map(w -> modelMapper.map(w, WarehouseResponse.class))
                .collect(Collectors.toList());
    }
}
