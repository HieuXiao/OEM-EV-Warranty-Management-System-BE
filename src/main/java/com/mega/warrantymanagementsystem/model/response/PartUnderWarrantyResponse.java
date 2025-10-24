package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  // sinh constructor có tham số
@NoArgsConstructor   // sinh constructor không tham số
public class PartUnderWarrantyResponse {
    private String partSerial;
    private String partName;
    private String partBranch;
    private Float price;
    private String vehicleModel;
    private String description;
    private String adminId; // liên kết với account quản lý part
}
