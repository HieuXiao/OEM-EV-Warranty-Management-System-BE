package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.ServiceAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ServiceAppointmentRepository extends JpaRepository<ServiceAppointment, Integer> {

    // Tìm tất cả lịch hẹn của một xe theo VIN
    List<ServiceAppointment> findByVehicle_Vin(String vin);

    // Tìm tất cả lịch hẹn theo campaignId
    List<ServiceAppointment> findByCampaign_CampaignId(int campaignId);

    // Tìm lịch hẹn trong khoảng thời gian
    List<ServiceAppointment> findByDateBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByVehicle_VinAndDate(String vin, LocalDateTime date);


}
