package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
public class ClaimPartCheckRequest {
    @NotEmpty(message = "partNumber cannot be empty")
    private String partNumber; // PK

    @NotEmpty(message = "warrantyId cannot be empty")
    private String warrantyId; // FK → WarrantyClaim.claimId

    @NotEmpty(message = "vin cannot be empty")
    private String vin; // FK → Vehicle.vin

    @NotNull(message = "quantity cannot be null")
    private Integer quantity;

    private Boolean isRepair;

    private String partId; // optional, link sang PartUnderWarranty


//    private String partSerial; // optional
}
