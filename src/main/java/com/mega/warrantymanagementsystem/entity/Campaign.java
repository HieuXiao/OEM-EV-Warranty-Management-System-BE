package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campaign")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    private int campaignId;

    @Column(name = "campaign_name", length = 100, nullable = false)
    private String campaignName;

    @Column(name = "service_description", columnDefinition = "TEXT")
    private String serviceDescription;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    // ====== Thêm trường model để xác định chiến dịch áp dụng cho dòng xe nào ======
    @Column(name = "model", nullable = false, length = 50)
    private String model;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vehicle> vehicles = new ArrayList<>();

    // Một Campaign có nhiều ServiceRecord
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ServiceRecord> serviceRecords = new ArrayList<>();

    // -----------------Map ngược lại Warranty Claim ------------------
    @ManyToMany(mappedBy = "campaigns")
    @JsonIgnore
    private List<WarrantyClaim> warrantyClaims = new ArrayList<>();


}
