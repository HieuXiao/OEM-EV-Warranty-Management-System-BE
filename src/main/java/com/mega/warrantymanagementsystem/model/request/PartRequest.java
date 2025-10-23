package com.mega.warrantymanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor  // để tiện tạo object
public class PartRequest {

    @NotEmpty(message = "Part ID cannot be empty!")
    private String partId;

    @NotEmpty(message = "Part name cannot be empty!")
    private String name;

    @NotEmpty(message = "Serial number cannot be empty!")
    private String serialNumber;

    private String description;

    @NotNull(message = "Inventory ID cannot be null!")
    private Integer inventoryId;
}
