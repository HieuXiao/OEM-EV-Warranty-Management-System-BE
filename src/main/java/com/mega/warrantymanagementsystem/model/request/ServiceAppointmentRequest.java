package com.mega.warrantymanagementsystem.model.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ServiceAppointmentRequest {
    private String vin;
    private Integer campaignId;
    private LocalDateTime appointmentDate;
    private String status;
    private String description;
}
