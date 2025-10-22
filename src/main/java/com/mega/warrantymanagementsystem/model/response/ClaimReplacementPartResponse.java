package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor  // để tiện tạo object
public class ClaimReplacementPartResponse {

    private int partUserId;

    private Integer partId;

    private Integer claimId;

    private Integer quantity;

    private String reason;

    private String description;

}
