package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "service_center")
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "center_id", nullable = false, unique = true)
    private int centerId;

    @Column(name = "center_name", nullable = false, length = 100)
    @NotEmpty(message = "centerName cannot be empty")
    private String centerName;

    @Column(name = "location", nullable = false, length = 200)
    @NotEmpty(message = "location cannot be empty")
    private String location;

    @OneToMany(mappedBy = "serviceCenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignReport> campaignReports;
}
