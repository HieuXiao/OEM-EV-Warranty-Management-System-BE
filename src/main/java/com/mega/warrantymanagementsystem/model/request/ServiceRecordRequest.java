package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecordRequest {

    @NotNull(message = "Vehicle VIN cannot be null")
    private String vin; // Tham chiếu đến Vehicle

    @NotNull(message = "ServiceAppointment ID cannot be null")
    private int serviceAppointmentId; // Tham chiếu đến ServiceAppointment

    @NotNull(message = "ServiceCenter ID cannot be null")
    private int serviceCenterId; // Tham chiếu đến ServiceCenter

    private String result;

    @NotEmpty(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "Service date cannot be null")
    private LocalDate serviceDate;

    private String description;

}
