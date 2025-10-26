package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class WarrantyFileRequest {
    @NotEmpty
    private String fileId;
    private String claimId;
    private List<String> mediaUrls;
}
