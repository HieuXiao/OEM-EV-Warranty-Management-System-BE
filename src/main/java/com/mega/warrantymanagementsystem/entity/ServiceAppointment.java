package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;


=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
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
    private LocalDateTime appointmentDate;

    @Column(name = "status", length = 50)
    @NotEmpty(message = "Status cannot be empty!")
    private String status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
<<<<<<< HEAD

    // Một lịch hẹn có thể tạo ra nhiều ServiceRecord
    @OneToMany(mappedBy = "serviceAppointment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ServiceRecord> serviceRecords = new ArrayList<>();
=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
}
