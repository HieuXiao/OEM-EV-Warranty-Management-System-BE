package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.ServiceAppointment;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.model.request.ServiceAppointmentRequest;
import com.mega.warrantymanagementsystem.repository.ServiceAppointmentRepository;
import com.mega.warrantymanagementsystem.service.ServiceAppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceAppointmentServiceImpl implements ServiceAppointmentService {

    @Autowired
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ServiceAppointment createAppointment(ServiceAppointmentRequest request) {
        // kiểm tra trùng lịch hẹn cùng VIN và cùng ngày giờ
        List<ServiceAppointment> existing = serviceAppointmentRepository.findByVin(request.getVin());
        boolean duplicated = existing.stream()
                .anyMatch(a -> a.getAppointmentDate().equals(request.getAppointmentDate()));
        if (duplicated) {
            throw new DuplicateResourceException("This VIN already has an appointment at that time!");
        }

        ServiceAppointment appointment = modelMapper.map(request, ServiceAppointment.class);
        return serviceAppointmentRepository.save(appointment);
    }

    @Override
    public List<ServiceAppointment> getAppointmentsByVin(String vin) {
        return serviceAppointmentRepository.findByVin(vin);
    }

    @Override
    public List<ServiceAppointment> getAppointmentsByCampaign(int campaignId) {
        return serviceAppointmentRepository.findByCampaign_CampaignId(campaignId);
    }

    @Override
    public List<ServiceAppointment> getAllAppointments() {
        return serviceAppointmentRepository.findAll();
    }
}
