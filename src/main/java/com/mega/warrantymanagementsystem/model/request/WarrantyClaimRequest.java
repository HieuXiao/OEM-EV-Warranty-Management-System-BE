package com.mega.warrantymanagementsystem.model.request;

import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
public class WarrantyClaimRequest {

    // Không có ID — vì claimId được tự động sinh

    @NotNull(message = "Vehicle VIN cannot be null")
    private String vin; // liên kết Vehicle

    @NotNull(message = "Service Center Staff ID cannot be null")
    private String scStaffId; // Account (staff)

    @NotNull(message = "Technician ID cannot be null")
    private String scTechnicianId; // Account (technician)

    @NotNull(message = "Policy ID cannot be null")
    private int policyId; // liên kết Policy

    private String description; // mô tả chi tiết

}
