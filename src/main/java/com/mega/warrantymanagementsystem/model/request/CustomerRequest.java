package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
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

