package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.ServiceAppointment;
import com.mega.warrantymanagementsystem.model.request.ServiceAppointmentRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServiceAppointmentService {

    ServiceAppointment createAppointment(ServiceAppointmentRequest request);

    List<ServiceAppointment> getAppointmentsByVin(String vin);

    List<ServiceAppointment> getAppointmentsByCampaign(int campaignId);

    List<ServiceAppointment> getAllAppointments();
}
