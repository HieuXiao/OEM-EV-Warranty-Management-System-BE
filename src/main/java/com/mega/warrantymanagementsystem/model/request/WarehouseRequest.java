package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class WarehouseRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String location;
}
