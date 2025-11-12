package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "warranty_claim")
@AllArgsConstructor
@NoArgsConstructor
public class WarrantyClaim {

    // ------------------ Khóa chính ------------------
    @Id
    @Column(name = "claim_id")
    private String claimId;

    // ------------------ Quan hệ với Vehicle ------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vin", referencedColumnName = "vin", nullable = false)
    private Vehicle vehicle;

    // ------------------ Quan hệ với Account (staff, technician, evm) ------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sc_staff_id", referencedColumnName = "accountId")
    private Account serviceCenterStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sc_technician_id", referencedColumnName = "accountId")
    private Account serviceCenterTechnician;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evm_id", referencedColumnName = "accountId")
    private Account evm;

    // ------------------ Ngày yêu cầu ------------------
    @Column(name = "claim_date", nullable = false)
    private LocalDate claimDate;

    // ------------------ Trạng thái yêu cầu ------------------
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private WarrantyClaimStatus status;

    // ------------------ Mô tả ------------------
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "evm_description", columnDefinition = "TEXT")
    private String evmDescription;

    // ----------------- Update status -----------
    @Column(name = "technician_done", nullable = false)
    private boolean technicianDone = false;

    @Column(name = "sc_staff_done", nullable = false)
    private boolean scStaffDone = false;

    @Column(name = "is_repair", nullable = false, columnDefinition = "boolean default true")
    private Boolean isRepair = true;

    // ------------------ Quan hệ với ClaimPartCheck ------------------
    @OneToMany(mappedBy = "warrantyClaim", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ClaimPartCheck> claimPartChecks = new ArrayList<>();

    // ------------------ Quan hệ với WarrantyFile ------------------
    @OneToMany(mappedBy = "warrantyClaim", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<WarrantyFile> warrantyFiles = new ArrayList<>();

    // ------------------- Quan hệ với Campaign -----------------------
    @ManyToMany
    @JoinTable(
            name = "claim_campaign",
            joinColumns = @JoinColumn(name = "claim_id"),
            inverseJoinColumns = @JoinColumn(name = "campaign_id")
    )
    @JsonIgnore
    private List<Campaign> campaigns = new ArrayList<>();

    /**
     * Đảm bảo khi tạo mới (persist) mà status chưa được set,
     * thì mặc định là CHECK.
     */
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = WarrantyClaimStatus.CHECK;
        }
    }

    @OneToMany(mappedBy = "warrantyClaim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarrantyClaimProgress> progressHistory = new ArrayList<>();

}
