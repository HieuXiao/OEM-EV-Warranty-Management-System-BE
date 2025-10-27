package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.ServiceCenter;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ServiceCenterRequest;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.repository.ServiceCenterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý logic cho ServiceCenter.
 * Bao gồm CRUD và tìm kiếm theo name, location.
 */
@Service
public class ServiceCenterService {

    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Tạo mới Service Center.
     */
    public ServiceCenterResponse create(ServiceCenterRequest request) {
        // check trùng name
        if (serviceCenterRepository.existsByCenterNameIgnoreCase(request.getCenterName())) {
            throw new DuplicateResourceException(
                    "Service Center name already exists: " + request.getCenterName()
            );
        }

        // check trùng location
        if (serviceCenterRepository.existsByLocationIgnoreCase(request.getLocation())) {
            throw new DuplicateResourceException(
                    "Service Center location already exists: " + request.getLocation()
            );
        }

        ServiceCenter center = modelMapper.map(request, ServiceCenter.class);
        ServiceCenter saved = serviceCenterRepository.save(center);
        return modelMapper.map(saved, ServiceCenterResponse.class);
    }

    /**
     * Cập nhật Service Center theo ID.
     */
    public ServiceCenterResponse update(Integer id, ServiceCenterRequest request) {
        ServiceCenter existing = serviceCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service Center không tồn tại với ID: " + id));

        // kiểm tra trùng nếu tên mới khác tên cũ
        if (!existing.getCenterName().equalsIgnoreCase(request.getCenterName())
                && serviceCenterRepository.existsByCenterNameIgnoreCase(request.getCenterName())) {
            throw new DuplicateResourceException("Service Center name already exists: " + request.getCenterName());
        }
        // check trùng location nếu khác location cũ
        if (!existing.getLocation().equalsIgnoreCase(request.getLocation())
                && serviceCenterRepository.existsByLocationIgnoreCase(request.getLocation())) {
            throw new DuplicateResourceException(
                    "Service Center location already exists: " + request.getLocation()
            );
        }

        existing.setCenterName(request.getCenterName());
        existing.setLocation(request.getLocation());

        ServiceCenter updated = serviceCenterRepository.save(existing);
        return modelMapper.map(updated, ServiceCenterResponse.class);
    }

    /**
     * Xóa Service Center.
     */
    public void delete(Integer id) {
        if (!serviceCenterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service Center không tồn tại với ID: " + id);
        }
        serviceCenterRepository.deleteById(id);
    }

    /**
     * Lấy tất cả Service Center.
     */
    public List<ServiceCenterResponse> getAll() {
        return serviceCenterRepository.findAll().stream()
                .map(center -> modelMapper.map(center, ServiceCenterResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy theo ID.
     */
    public ServiceCenterResponse getById(Integer id) {
        ServiceCenter center = serviceCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service Center không tồn tại với ID: " + id));
        return modelMapper.map(center, ServiceCenterResponse.class);
    }

    /**
     * Tìm theo tên trung tâm.
     */
    public List<ServiceCenterResponse> getByName(String name) {
        return serviceCenterRepository.findAll().stream()
                .filter(c -> c.getCenterName() != null && c.getCenterName().toLowerCase().contains(name.toLowerCase()))
                .map(c -> modelMapper.map(c, ServiceCenterResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo vị trí (location).
     */
    public List<ServiceCenterResponse> getByLocation(String location) {
        return serviceCenterRepository.findAll().stream()
                .filter(c -> c.getLocation() != null && c.getLocation().toLowerCase().contains(location.toLowerCase()))
                .map(c -> modelMapper.map(c, ServiceCenterResponse.class))
                .collect(Collectors.toList());
    }
}
