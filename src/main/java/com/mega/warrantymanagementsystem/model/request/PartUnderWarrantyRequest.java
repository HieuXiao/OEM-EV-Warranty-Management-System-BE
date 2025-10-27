package com.mega.warrantymanagementsystem.model.request;

import lombok.Data;

@Data
public class PartUnderWarrantyRequest {
    private String partId;
    private String adminId;
    private String partName;
    private String partBrand;
    private Float price;
    private String vehicleModel;
    private String description;
    private Boolean isEnable;
}
