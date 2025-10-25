//package com.mega.warrantymanagementsystem.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
///**
// * Entity đại diện cho bảng service_records.
// * Mỗi ServiceRecord thể hiện kết quả của một lần bảo dưỡng / sửa chữa cụ thể.
// *
// * Mối quan hệ:
// *  - Nhiều ServiceRecord thuộc về 1 ServiceCenter.
// *  - Nhiều ServiceRecord thuộc về 1 Vehicle.
// *  - Nhiều ServiceRecord thuộc về 1 ServiceAppointment.
// *  - Nhiều ServiceRecord thuộc về 1 Campaign.
// */
//@Entity
//@Table(name = "service_records")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class ServiceRecord {
//
//    //==================== Khóa chính ====================//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "record_id")
//    private Integer recordId;
//
//    //==================== Quan hệ với Vehicle ====================//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "vin", nullable = false)
//    @JsonIgnore
//    private Vehicle vehicle; // Mỗi ServiceRecord thuộc về 1 Vehicle
//
//    //==================== Quan hệ với ServiceAppointment ====================//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "appointment_id", nullable = false)
//    @JsonIgnore
//    private ServiceAppointment serviceAppointment; // Mỗi record thuộc 1 Appointment
//
//    //==================== Quan hệ với Campaign ====================//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "campaign_id", nullable = true)
//    @JsonIgnore
//    private Campaign campaign; // Mỗi record thuộc về 1 Campaign
//
//    //==================== Quan hệ với ServiceCenter ====================//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "center_id", nullable = false)
//    @JsonIgnore
//    private ServiceCenter serviceCenter; // Mỗi record thực hiện tại 1 ServiceCenter
//
//    //==================== Các thông tin nghiệp vụ ====================//
//    @Column(name = "result", columnDefinition = "TEXT")
//    private String result;
//
//    @Column(name = "status", nullable = false, length = 20)
//    @NotEmpty(message = "Status cannot be empty")
//    private String status;
//
//    @Column(name = "service_date", nullable = false)
//    @NotNull(message = "Service date cannot be null")
//    private LocalDate serviceDate;
//
//    @Column(name = "description", columnDefinition = "TEXT")
//    private String description;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "claim_id")
//    @JsonIgnore
//    private WarrantyClaim warrantyClaim;
//
//}
