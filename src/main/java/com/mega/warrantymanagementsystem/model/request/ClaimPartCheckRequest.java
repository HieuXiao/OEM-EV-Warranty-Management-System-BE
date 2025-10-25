package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimPartCheckRequest {
    @NotEmpty
    private String partSerialId;

    @NotNull
    private Integer claimId;

    @NotNull
    private Integer quantity;

    private boolean isRepair;
}
