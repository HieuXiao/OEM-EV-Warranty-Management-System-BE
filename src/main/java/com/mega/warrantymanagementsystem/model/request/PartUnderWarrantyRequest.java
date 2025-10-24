package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartUnderWarrantyRequest {

    @NotEmpty(message = "Part serial cannot be empty!")
    private String partSerial;

    @NotEmpty(message = "Part name cannot be empty!")
    private String partName;

    private String partBranch;

    @NotNull(message = "Price cannot be null!")
    private Float price;

    private String vehicleModel;

    private String description;
}