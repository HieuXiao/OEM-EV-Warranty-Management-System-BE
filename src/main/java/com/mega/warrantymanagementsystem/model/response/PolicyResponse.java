package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyResponse {
    private int policyId;
    private int kilometer;
    private String policyPart;
    private String policyModel;
    private int policyYear;
}
