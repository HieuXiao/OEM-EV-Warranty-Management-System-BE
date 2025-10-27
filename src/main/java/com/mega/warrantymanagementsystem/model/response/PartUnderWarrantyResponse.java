package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

@Data
public class PartUnderWarrantyResponse {
    private String partSerial;
    private String partName;
    private String partBrand;
    private Float price;
    private String vehicleModel;
    private String description;
    private Boolean isEnable;
    private AccountResponse admin;
}
