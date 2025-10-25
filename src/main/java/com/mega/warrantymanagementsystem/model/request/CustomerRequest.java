package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotEmpty
    private String customerName;

    @NotEmpty
    private String customerPhone;

    @Email
    private String customerEmail;

    @NotEmpty
    private String customerAddress;
}

