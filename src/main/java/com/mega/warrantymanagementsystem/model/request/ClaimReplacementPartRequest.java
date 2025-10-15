package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor  // để tiện tạo object
public class ClaimReplacementPartRequest {

    @NotEmpty(message = "Id cannot be null")
    private int partUserId;

}
