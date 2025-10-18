package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.ServiceCenter;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ServiceCenterRequest;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.repository.ServiceCenterRepository;
import com.mega.warrantymanagementsystem.service.ServiceCenterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceCenterServiceImpl implements ServiceCenterService {

    @Autowired
    ServiceCenterRepository serviceCenterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ServiceCenterResponse createServiceCenter(ServiceCenterRequest serviceCenterRequest) {
        // 1. Kiểm tra trung tâm có bị trùng tên không
        ServiceCenter existing = serviceCenterRepository.findByCenterName(serviceCenterRequest.getCenterName());
        if (existing != null) {
            throw new DuplicateResourceException(
            "Service Center with name already exists: " + serviceCenterRequest.getCenterName());
         }

        // 2. Map từ request sang entity
        ServiceCenter serviceCenter = modelMapper.map(serviceCenterRequest, ServiceCenter.class);

        // 3. Lưu vào cơ sở dữ liệu
        ServiceCenter saved = serviceCenterRepository.save(serviceCenter);

        // 4. Map lại sang response
        return modelMapper.map(saved, ServiceCenterResponse.class);
    }

    @Override
    public ServiceCenterResponse findById(int id) {
        Optional<ServiceCenter> optional = serviceCenterRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("ServiceCenter not found with id: " + id);
        }
        ServiceCenter serviceCenter = optional.get();
        return modelMapper.map(serviceCenter, ServiceCenterResponse.class);
    }


    @Override
    public List<ServiceCenterResponse> findByLocation(String location) {
        List<ServiceCenter> serviceCenters =
            serviceCenterRepository.findByLocation(location);
        List<ServiceCenterResponse> serviceCenterResponses = new ArrayList<>();
        for(ServiceCenter s : serviceCenters){
            serviceCenterResponses.add(modelMapper.map(s, ServiceCenterResponse.class));
        }
     return serviceCenterResponses;
    }

    @Override
    public List<ServiceCenterResponse> findAllServiceCenter() {

        List<ServiceCenter> serviceCenters =
            serviceCenterRepository.findAll();
        List<ServiceCenterResponse> serviceCenterResponses = new ArrayList<>();
        for(ServiceCenter s : serviceCenters){
            serviceCenterResponses.add(modelMapper.map(s, ServiceCenterResponse.class));
        }
        return serviceCenterResponses;
    }

    @Override
    public ServiceCenterResponse findByName(String name) {
        ServiceCenter serviceCenter = serviceCenterRepository.findByCenterName(name);
        if (serviceCenter == null) {
            throw new ResourceNotFoundException("Service center not found with name: " + name);
        }
        return modelMapper.map(serviceCenter, ServiceCenterResponse.class);
    }

    @Override
    public ServiceCenterResponse updateServiceCenter(int id, ServiceCenterRequest request) {
        Optional<ServiceCenter> optional = serviceCenterRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Service center not found with id: " + id);
        }

        ServiceCenter serviceCenter = optional.get();
        serviceCenter.setCenterName(request.getCenterName());
        serviceCenter.setLocation(request.getLocation());

        ServiceCenter updated = serviceCenterRepository.save(serviceCenter);
        return modelMapper.map(updated, ServiceCenterResponse.class);
    }

    @Override
    public void deleteServiceCenter(int id) {
        Optional<ServiceCenter> optional = serviceCenterRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Service center not found with id: " + id);
        }

        ServiceCenter existing = optional.get();
        serviceCenterRepository.delete(existing);
    }


}
