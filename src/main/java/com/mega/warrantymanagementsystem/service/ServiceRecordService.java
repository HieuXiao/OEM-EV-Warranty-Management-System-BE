//package com.mega.warrantymanagementsystem.service;
//
//import com.mega.warrantymanagementsystem.model.request.ServiceRecordRequest;
//import com.mega.warrantymanagementsystem.model.response.ServiceRecordResponse;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public interface ServiceRecordService {
//
//    ServiceRecordResponse createServiceRecord(ServiceRecordRequest request);
//
//    ServiceRecordResponse updateServiceRecord(int recordId, ServiceRecordRequest request);
//
//    ServiceRecordResponse findById(int recordId);
//
//    List<ServiceRecordResponse> findAllServiceRecords();
//
//    void deleteServiceRecord(int recordId);
//
//    // Các phương thức tìm kiếm nâng cao
//    List<ServiceRecordResponse> findByVehicleVin(String vin);
//
//    List<ServiceRecordResponse> findByCampaignId(int campaignId);
//
//    List<ServiceRecordResponse> findByServiceCenterId(int centerId);
//
//    List<ServiceRecordResponse> findByAppointmentId(int appointmentId);
//
//    void assignCampaignToRecord(int recordId, int campaignId);
//
//    void removeCampaignFromRecord(int recordId);
//
//}
