package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRequest {

    @NotNull(message = "Kilometer cannot be null!")
    private Integer kilometer;

    @NotEmpty(message = "Policy part cannot be empty!")
    @Size(max = 10, message = "Policy part must be less than or equal to 10 characters")
    private String policyPart;

    @NotEmpty(message = "Policy model cannot be empty!")
    @Size(max = 50, message = "Policy model must be less than or equal to 50 characters")
    private String policyModel;

    @NotNull(message = "Policy year cannot be null!")
    private Integer policyYear;
}
