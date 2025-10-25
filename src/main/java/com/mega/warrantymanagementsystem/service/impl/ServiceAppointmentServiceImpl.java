//package com.mega.warrantymanagementsystem.service.impl;
//
//import com.mega.warrantymanagementsystem.entity.Campaign;
//import com.mega.warrantymanagementsystem.entity.ServiceAppointment;
//import com.mega.warrantymanagementsystem.entity.Vehicle;
//import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.ServiceAppointmentRequest;
//import com.mega.warrantymanagementsystem.repository.CampaignRepository;
//import com.mega.warrantymanagementsystem.repository.ServiceAppointmentRepository;
//import com.mega.warrantymanagementsystem.repository.VehicleRepository;
//import com.mega.warrantymanagementsystem.service.ServiceAppointmentService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ServiceAppointmentServiceImpl implements ServiceAppointmentService {
//
//    @Autowired
//    private ServiceAppointmentRepository serviceAppointmentRepository;
//
//    @Autowired
//    private VehicleRepository vehicleRepository;
//
//    @Autowired
//    private CampaignRepository campaignRepository;
//
//    @Override
//    public ServiceAppointment createAppointment(ServiceAppointmentRequest request) {
//        // check trùng lịch hẹn cùng VIN và cùng ngày giờ
//        List<ServiceAppointment> existing = serviceAppointmentRepository.findByVehicle_Vin(request.getVin());
//        boolean duplicated = existing.stream()
//                .anyMatch(a -> a.getAppointmentDate().equals(request.getAppointmentDate()));
//        if (duplicated) {
//            throw new DuplicateResourceException("This VIN already has an appointment at that time!");
//        }
//
//        // lấy vehicle từ DB
//        Vehicle vehicle = vehicleRepository.findById(request.getVin())
//                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with VIN: " + request.getVin()));
//
//        // lấy campaign từ DB
//        Campaign campaign = campaignRepository.findById(request.getCampaignId())
//                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + request.getCampaignId()));
//
//        // tạo mới ServiceAppointment
//        ServiceAppointment appointment = new ServiceAppointment();
//        appointment.setVehicle(vehicle);
//        appointment.setCampaign(campaign);
//        appointment.setAppointmentDate(request.getAppointmentDate());
//        appointment.setStatus(request.getStatus());
//        appointment.setDescription(request.getDescription());
//
//        return serviceAppointmentRepository.save(appointment);
//    }
//
//    @Override
//    public List<ServiceAppointment> getAppointmentsByVin(String vin) {
//        return serviceAppointmentRepository.findByVehicle_Vin(vin);
//    }
//
//    @Override
//    public List<ServiceAppointment> getAppointmentsByCampaign(int campaignId) {
//        return serviceAppointmentRepository.findByCampaign_CampaignId(campaignId);
//    }
//
//    @Override
//    public List<ServiceAppointment> getAllAppointments() {
//        return serviceAppointmentRepository.findAll();
//    }
//}
