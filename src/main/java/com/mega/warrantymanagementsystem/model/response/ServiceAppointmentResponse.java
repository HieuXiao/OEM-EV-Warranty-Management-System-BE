package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
public class ServiceAppointmentResponse {

    private int appointmentId;

    private String vin;

    private int campaignId;

    private LocalDateTime appointmentDate;

    private String status;

    private String description;
}
