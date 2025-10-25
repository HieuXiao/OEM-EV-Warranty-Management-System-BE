package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "claim_part_check")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimPartCheck {

    @Id
    @Column(name = "part_serial_id", nullable = false, unique = true)
    private String partSerialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    @JsonIgnore
    private WarrantyClaim warrantyClaim;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "isRepair")
    private boolean isRepair;
}
