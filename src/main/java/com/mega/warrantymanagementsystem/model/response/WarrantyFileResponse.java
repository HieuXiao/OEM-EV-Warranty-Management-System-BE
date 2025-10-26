package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;
import java.util.List;

@Data
public class WarrantyFileResponse {
    private String fileId;
    private List<String> mediaUrls;
    private String claimId;
}
