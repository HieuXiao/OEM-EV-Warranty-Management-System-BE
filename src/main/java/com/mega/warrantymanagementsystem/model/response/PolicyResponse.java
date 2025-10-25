package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

@Data
public class PolicyResponse {
    private int policyId;
    private String policyName;
    private int availableYear;
    private int kilometer;
    private Boolean isEnable;
    private PartUnderWarrantyResponse partUnderWarranty;
}
