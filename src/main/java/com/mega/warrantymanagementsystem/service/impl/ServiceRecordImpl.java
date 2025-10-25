//package com.mega.warrantymanagementsystem.service.impl;
//
//import com.mega.warrantymanagementsystem.entity.*;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.ServiceRecordRequest;
//import com.mega.warrantymanagementsystem.model.response.ServiceRecordResponse;
//import com.mega.warrantymanagementsystem.repository.*;
//import com.mega.warrantymanagementsystem.service.ServiceRecordService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class ServiceRecordImpl implements ServiceRecordService {
//
//    @Autowired
//    private ServiceRecordRepository serviceRecordRepository;
//
//    @Autowired
//    private VehicleRepository vehicleRepository;
//
//    @Autowired
//    private CampaignRepository campaignRepository;
//
//    @Autowired
//    private ServiceAppointmentRepository serviceAppointmentRepository;
//
//    @Autowired
//    private ServiceCenterRepository serviceCenterRepository;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    //=========================================
//    // TẠO SERVICE RECORD
//    //=========================================
//    @Override
//    public ServiceRecordResponse createServiceRecord(ServiceRecordRequest request) {
//
//        Vehicle vehicle = vehicleRepository.findById(request.getVin())
//                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with VIN: " + request.getVin()));
//
//        ServiceAppointment appointment = serviceAppointmentRepository.findById(request.getServiceAppointmentId())
//                .orElseThrow(() -> new ResourceNotFoundException("ServiceAppointment not found with ID: " + request.getServiceAppointmentId()));
//
//        ServiceCenter serviceCenter = serviceCenterRepository.findById(request.getServiceCenterId())
//                .orElseThrow(() -> new ResourceNotFoundException("ServiceCenter not found with ID: " + request.getServiceCenterId()));
//
//        ServiceRecord record = new ServiceRecord();
//        record.setResult(request.getResult());
//        record.setStatus(request.getStatus());
//        record.setServiceDate(request.getServiceDate());
//        record.setDescription(request.getDescription());
//        record.setVehicle(vehicle);
//        record.setServiceAppointment(appointment);
//        record.setServiceCenter(serviceCenter);
//        record.setRecordId(null);
//
//        ServiceRecord saved = serviceRecordRepository.save(record);
//        return mapToResponse(saved);
//    }
//
//    //=========================================
//    // CẬP NHẬT
//    //=========================================
//    @Override
//    public ServiceRecordResponse updateServiceRecord(int recordId, ServiceRecordRequest request) {
//        Optional<ServiceRecord> optional = serviceRecordRepository.findById(recordId);
//        if (optional.isEmpty()) {
//            throw new ResourceNotFoundException("ServiceRecord not found with ID: " + recordId);
//        }
//
//        ServiceRecord existing = optional.get();
//        existing.setStatus(request.getStatus());
//        existing.setResult(request.getResult());
//        existing.setDescription(request.getDescription());
//        existing.setServiceDate(request.getServiceDate());
//
//        ServiceRecord updated = serviceRecordRepository.save(existing);
//        return mapToResponse(updated);
//    }
//
//    //=========================================
//    // TÌM THEO ID
//    //=========================================
//    @Override
//    public ServiceRecordResponse findById(int recordId) {
//        Optional<ServiceRecord> optional = serviceRecordRepository.findById(recordId);
//        if (optional.isEmpty()) {
//            throw new ResourceNotFoundException("ServiceRecord not found with ID: " + recordId);
//        }
//
//        return mapToResponse(optional.get());
//    }
//
//    //=========================================
//    // LẤY TOÀN BỘ
//    //=========================================
//    @Override
//    public List<ServiceRecordResponse> findAllServiceRecords() {
//        List<ServiceRecord> records = serviceRecordRepository.findAll();
//        List<ServiceRecordResponse> responses = new ArrayList<>();
//        for (ServiceRecord record : records) {
//            responses.add(mapToResponse(record));
//        }
//        return responses;
//    }
//
//    //=========================================
//    // XÓA
//    //=========================================
//    @Override
//    public void deleteServiceRecord(int recordId) {
//        Optional<ServiceRecord> optional = serviceRecordRepository.findById(recordId);
//        if (optional.isEmpty()) {
//            throw new ResourceNotFoundException("ServiceRecord not found with ID: " + recordId);
//        }
//        serviceRecordRepository.delete(optional.get());
//    }
//
//    //=========================================
//    // TÌM THEO VEHICLE VIN
//    //=========================================
//    @Override
//    public List<ServiceRecordResponse> findByVehicleVin(String vin) {
//        List<ServiceRecord> records = serviceRecordRepository.findByVehicle_Vin(vin);
//        List<ServiceRecordResponse> responses = new ArrayList<>();
//        for (ServiceRecord record : records) {
//            responses.add(mapToResponse(record));
//        }
//        return responses;
//    }
//
//    //=========================================
//    // TÌM THEO CAMPAIGN
//    //=========================================
//    @Override
//    public List<ServiceRecordResponse> findByCampaignId(int campaignId) {
//        List<ServiceRecord> records = serviceRecordRepository.findByCampaign_CampaignId(campaignId);
//        List<ServiceRecordResponse> responses = new ArrayList<>();
//        for (ServiceRecord record : records) {
//            responses.add(mapToResponse(record));
//        }
//        return responses;
//    }
//
//    //=========================================
//    // TÌM THEO SERVICE CENTER
//    //=========================================
//    @Override
//    public List<ServiceRecordResponse> findByServiceCenterId(int centerId) {
//        List<ServiceRecord> records = serviceRecordRepository.findByServiceCenter_CenterId(centerId);
//        List<ServiceRecordResponse> responses = new ArrayList<>();
//        for (ServiceRecord record : records) {
//            responses.add(mapToResponse(record));
//        }
//        return responses;
//    }
//
//    //=========================================
//    // TÌM THEO APPOINTMENT
//    //=========================================
//    @Override
//    public List<ServiceRecordResponse> findByAppointmentId(int appointmentId) {
//        List<ServiceRecord> records = serviceRecordRepository.findByServiceAppointment_AppointmentId(appointmentId);
//        List<ServiceRecordResponse> responses = new ArrayList<>();
//        for (ServiceRecord record : records) {
//            responses.add(mapToResponse(record));
//        }
//        return responses;
//    }
//
//    //=========================================
//    // HÀM MAP CHUẨN HOÁ RESPONSE
//    //=========================================
//    private ServiceRecordResponse mapToResponse(ServiceRecord record) {
//        ServiceRecordResponse response = modelMapper.map(record, ServiceRecordResponse.class);
//
//        if (record.getVehicle() != null) {
//            response.setVin(record.getVehicle().getVin());
//        }
//        if (record.getCampaign() != null) {
//            response.setCampaignId(record.getCampaign().getCampaignId());
//        }
//        if (record.getServiceAppointment() != null) {
//            response.setServiceAppointmentId(record.getServiceAppointment().getAppointmentId());
//        }
//        if (record.getServiceCenter() != null) {
//            response.setServiceCenterId(record.getServiceCenter().getCenterId());
//        }
//
//        return response;
//    }
//
//    @Override
//    public void assignCampaignToRecord(int recordId, int campaignId) {
//        ServiceRecord record = serviceRecordRepository.findById(recordId)
//                .orElseThrow(() -> new ResourceNotFoundException("ServiceRecord not found with ID: " + recordId));
//
//        Campaign campaign = campaignRepository.findById(campaignId)
//                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + campaignId));
//
//        record.setCampaign(campaign);
//        serviceRecordRepository.save(record);
//    }
//
//    @Override
//    public void removeCampaignFromRecord(int recordId) {
//        ServiceRecord record = serviceRecordRepository.findById(recordId)
//                .orElseThrow(() -> new ResourceNotFoundException("ServiceRecord not found with ID: " + recordId));
//
//        record.setCampaign(null);
//        serviceRecordRepository.save(record);
//    }
//
//
//}
