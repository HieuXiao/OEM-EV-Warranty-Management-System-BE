package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor  // để tiện tạo object
public class ClaimReplacementPartRequest {

    @NotNull(message = "Part ID cannot be null")
    private Integer partId;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    private String reason;
    private String description;

    private Integer claimId;

}
