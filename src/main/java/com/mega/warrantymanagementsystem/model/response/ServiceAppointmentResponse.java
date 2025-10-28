package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ServiceAppointmentResponse {
    private int appointmentId;
    private LocalDateTime date;
    private String description;
    private String status;
    private VehicleResponse vehicle;
    private CampaignResponse campaign;
}
