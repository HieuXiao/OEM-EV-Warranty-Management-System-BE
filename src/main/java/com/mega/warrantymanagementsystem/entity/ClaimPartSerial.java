package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "claim_part_serial",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"serial_code", "claim_part_number"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimPartSerial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_code", length = 50, nullable = false)
    private String serialCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_part_number", nullable = false)
    @JsonIgnore
    private ClaimPartCheck claimPartCheck;
}
