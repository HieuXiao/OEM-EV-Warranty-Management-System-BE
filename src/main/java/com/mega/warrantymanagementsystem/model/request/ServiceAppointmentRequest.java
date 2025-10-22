package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
public class ServiceAppointmentRequest {

    private int appointmentId;

    @NotEmpty(message = "VIN cannot be empty!")
    private String vin;

    @NotNull(message = "Campaign ID cannot be null!")
    private Integer campaignId;

    @NotNull(message = "Appointment date cannot be null!")
    private LocalDateTime appointmentDate;

    @NotEmpty(message = "Status cannot be empty!")
    private String status;

    private String description;
}
