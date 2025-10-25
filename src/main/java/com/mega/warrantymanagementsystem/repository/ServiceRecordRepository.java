//package com.mega.warrantymanagementsystem.repository;
//
//import com.mega.warrantymanagementsystem.entity.ServiceRecord;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Integer> {
//
//    // Tìm theo VIN (vehicle)
//    List<ServiceRecord> findByVehicle_Vin(String vin);
//
//    // Tìm theo campaignId
//    List<ServiceRecord> findByCampaign_CampaignId(int campaignId);
//
//    // Tìm theo serviceCenterId
//    List<ServiceRecord> findByServiceCenter_CenterId(int centerId);
//
//    // Tìm theo appointmentId
//    List<ServiceRecord> findByServiceAppointment_AppointmentId(int appointmentId);
//
//}
