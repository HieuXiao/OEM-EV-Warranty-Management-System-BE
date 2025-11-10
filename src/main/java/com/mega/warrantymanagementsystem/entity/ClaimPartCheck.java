package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "claim_part_check")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimPartCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_number", length = 50)
    private String partNumber;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warranty_id", nullable = false)
    @JsonIgnore
    private WarrantyClaim warrantyClaim; // FK → WarrantyClaim.claimId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vin", nullable = false)
    @JsonIgnore
    private Vehicle vehicle; // FK → Vehicle.vin

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "part_id", nullable = true)
    private PartUnderWarranty partUnderWarranty; // FK → PartUnderWarranty.partId

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "is_repair", nullable = false)
    private Boolean isRepair;

    @OneToMany(mappedBy = "claimPartCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimPartSerial> partSerials; // optional, unique identifier
}
