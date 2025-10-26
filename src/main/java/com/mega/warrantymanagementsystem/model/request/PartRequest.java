package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PartRequest {
    @NotEmpty
    private String partNumber;

    @NotEmpty
    private String namePart;

    private int quantity;
    private float price;
    private Integer whId;
}
