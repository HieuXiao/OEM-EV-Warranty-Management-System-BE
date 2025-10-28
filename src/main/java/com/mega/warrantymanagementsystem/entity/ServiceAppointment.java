package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "service_appointment")
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id", nullable = false, unique = true)
    private int appointmentId;

    @ManyToOne
    @JoinColumn(name = "vin", nullable = false)
    @JsonIgnore
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    @JsonIgnore
    private Campaign campaign;

    @Column(name = "appointment_date", nullable = false)
    @NotNull(message = "Appointment date cannot be null!")
    private LocalDateTime date;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = true, length = 50)
    private String status;
}
