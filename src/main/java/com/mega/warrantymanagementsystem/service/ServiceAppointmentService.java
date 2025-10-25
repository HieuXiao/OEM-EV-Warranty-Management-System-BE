package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.ServiceAppointment;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.entity.Campaign;
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
        ServiceAppointment appointment = new ServiceAppointment();

        // 1️⃣ Map các field cơ bản
        appointment.setDate(request.getDate());
        appointment.setDescription(request.getDescription());

        // 2️⃣ Liên kết Vehicle
        Vehicle vehicle = vehicleRepository.findById(request.getVin())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Vehicle với VIN: " + request.getVin()));
        appointment.setVehicle(vehicle);

        // 3️⃣ Liên kết Campaign
        Campaign campaign = campaignRepository.findById(request.getCampaignId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Campaign với ID: " + request.getCampaignId()));
        appointment.setCampaign(campaign);

        // 4️⃣ Lưu và trả về response
        ServiceAppointment saved = serviceAppointmentRepository.save(appointment);
        return modelMapper.map(saved, ServiceAppointmentResponse.class);
    }

    /**
     * Cập nhật appointment.
     */
    public ServiceAppointmentResponse update(Integer id, ServiceAppointmentRequest request) {
        ServiceAppointment existing = serviceAppointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment không tồn tại!"));

        existing.setDate(request.getDate());
        existing.setDescription(request.getDescription());

        if (request.getVin() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Vehicle với VIN: " + request.getVin()));
            existing.setVehicle(vehicle);
        }

        if (request.getCampaignId() != null) {
            Campaign campaign = campaignRepository.findById(request.getCampaignId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Campaign với ID: " + request.getCampaignId()));
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
            throw new RuntimeException("Appointment không tồn tại!");
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
                .orElseThrow(() -> new RuntimeException("Appointment không tồn tại!"));
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
}
