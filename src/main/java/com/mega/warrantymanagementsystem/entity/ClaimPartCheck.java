package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "claim_part_check")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimPartCheck {

    @Id
    @Column(name = "part_number", length = 17)
    private String partNumber; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    @JsonIgnore
    private WarrantyClaim warrantyClaim; // FK → WarrantyClaim.claimId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vin", nullable = false)
    @JsonIgnore
    private Vehicle vehicle; // FK → Vehicle.vin

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "is_repair", nullable = false)
    private boolean isRepair;

    @Column(name = "part_serial", length = 33)
    private String partSerial; // optional, unique identifier
}
