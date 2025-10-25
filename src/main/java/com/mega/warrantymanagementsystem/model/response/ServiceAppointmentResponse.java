package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ServiceAppointmentResponse {
    private int appointmentId;
    private LocalDateTime date;
    private String description;
    private VehicleResponse vehicle;
    private CampaignResponse campaign;
}
