package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PolicyRequest {
    @NotEmpty
    private String policyName;

    @NotNull
    private Integer availableYear;

    @NotNull
    private Integer kilometer;

    private Boolean isEnable;

    private String partSerial; // ID của PartUnderWarranty
}
