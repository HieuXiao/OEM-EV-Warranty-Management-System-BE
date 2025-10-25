package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

@Data
public class PolicyResponse {
    private int policyId;
    private String policyName;
    private int availableYear;
    private int kilometer;
    private boolean isEnable;
    private PartUnderWarrantyResponse partUnderWarranty;
}
