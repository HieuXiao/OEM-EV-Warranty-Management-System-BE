package com.mega.warrantymanagementsystem.model.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ServiceAppointmentRequest {
    private String vin;
    private Integer campaignId;
    private LocalDateTime date; // đổi tên để khớp với entity
    private String description;

}
