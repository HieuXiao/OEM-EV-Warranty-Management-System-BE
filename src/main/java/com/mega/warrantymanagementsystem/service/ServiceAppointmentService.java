package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.ServiceAppointment;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.entity.Campaign;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ServiceAppointmentRequest;
import com.mega.warrantymanagementsystem.model.response.ServiceAppointmentResponse;
import com.mega.warrantymanagementsystem.repository.ServiceAppointmentRepository;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
import com.mega.warrantymanagementsystem.repository.CampaignRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý logic cho ServiceAppointment.
 * Bao gồm CRUD và search theo date, description.
 */
@Service
public class ServiceAppointmentService {

    @Autowired
    private ServiceAppointmentRepository serviceAppointmentRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Tạo mới appointment.
     * Liên kết vehicle và campaign theo ID, sau đó lưu.
     */
    public ServiceAppointmentResponse create(ServiceAppointmentRequest request) {
        if (request.getDate() == null) {
            throw new IllegalArgumentException("Appointment date cannot be null!");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVin())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with VIN: " + request.getVin()));

        Campaign campaign = campaignRepository.findById(request.getCampaignId())
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + request.getCampaignId()));

        boolean exists = serviceAppointmentRepository.existsByVehicle_VinAndDate(request.getVin(), request.getDate());
        if (exists) {
            throw new IllegalArgumentException("An appointment already exists for this vehicle on the selected date!");
        }

        ServiceAppointment appointment = new ServiceAppointment();
        appointment.setDate(request.getDate());
        appointment.setDescription(request.getDescription());
        appointment.setVehicle(vehicle);
        appointment.setCampaign(campaign);
        appointment.setStatus("Scheduled"); // optional

        ServiceAppointment saved = serviceAppointmentRepository.save(appointment);
        return modelMapper.map(saved, ServiceAppointmentResponse.class);
    }

    /**
     * Cập nhật appointment.
     */
    public ServiceAppointmentResponse update(Integer id, ServiceAppointmentRequest request) {

        ServiceAppointment existing = serviceAppointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service appointment not found with ID: " + id));

        if (request.getDate() == null) {
            throw new IllegalArgumentException("Appointment date cannot be null!");
        }

        if (request.getVin() != null) {
            boolean exists = serviceAppointmentRepository.existsByVehicle_VinAndDate(request.getVin(), request.getDate());
            if (exists && !existing.getVehicle().getVin().equals(request.getVin())) {
                throw new IllegalArgumentException("Another appointment already exists for this vehicle on the selected date!");
            }
        }

        existing.setDate(request.getDate());
        existing.setDescription(request.getDescription());

        if (request.getVin() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with VIN: " + request.getVin()));
            existing.setVehicle(vehicle);
        }

        if (request.getCampaignId() != null) {
            Campaign campaign = campaignRepository.findById(request.getCampaignId())
                    .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with ID: " + request.getCampaignId()));
            existing.setCampaign(campaign);
        }

        ServiceAppointment updated = serviceAppointmentRepository.save(existing);
        return modelMapper.map(updated, ServiceAppointmentResponse.class);
    }

    /**
     * Xóa appointment.
     */
    public void delete(Integer id) {
        if (!serviceAppointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service appointment not found with ID: " + id);
        }
        serviceAppointmentRepository.deleteById(id);
    }

    /**
     * Lấy tất cả appointment.
     */
    public List<ServiceAppointmentResponse> getAll() {
        return serviceAppointmentRepository.findAll().stream()
                .map(a -> modelMapper.map(a, ServiceAppointmentResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Lấy appointment theo ID.
     */
    public ServiceAppointmentResponse getById(Integer id) {
        ServiceAppointment appointment = serviceAppointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service appointment not found with ID: " + id));
        return modelMapper.map(appointment, ServiceAppointmentResponse.class);
    }

    /**
     * Tìm appointment theo mô tả.
     */
    public List<ServiceAppointmentResponse> getByDescription(String description) {
        return serviceAppointmentRepository.findAll().stream()
                .filter(a -> a.getDescription() != null &&
                        a.getDescription().toLowerCase().contains(description.toLowerCase()))
                .map(a -> modelMapper.map(a, ServiceAppointmentResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Tìm appointment theo ngày (LocalDate).
     */
    public List<ServiceAppointmentResponse> getByDate(LocalDate date) {
        return serviceAppointmentRepository.findAll().stream()
                .filter(a -> a.getDate() != null && a.getDate().isEqual(date))
                .map(a -> modelMapper.map(a, ServiceAppointmentResponse.class))
                .collect(Collectors.toList());
    }

    public String updateStatus(int appointmentId, String newStatus) {
        // check if appointment exists
        ServiceAppointment appointment = serviceAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service appointment not found with ID: " + appointmentId));

        // validate status
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty!");
        }

        // update and save
        appointment.setStatus(newStatus.trim());
        serviceAppointmentRepository.save(appointment);

        return "Status updated successfully for appointment ID " + appointmentId
                + " to: " + newStatus;
    }
}
