package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policyId")
    private int policyId;

    @Column(name = "policyName", length = 100, nullable = false)
    @NotEmpty(message = "Policy name cannot be empty")
    private String policyName;

    @Column(name = "availableYear", nullable = false)
    private int availableYear;

    @Column(name = "Kilometer", nullable = false)
    private int kilometer;

    @Column(name = "isEnable", nullable = false)
    private Boolean isEnable;

    // Liên kết đến bảng Parts_Under_Warranty
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private PartUnderWarranty partUnderWarranty;
}
