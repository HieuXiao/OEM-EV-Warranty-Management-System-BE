package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.model.request.PolicyRequest;
import com.mega.warrantymanagementsystem.model.response.PolicyResponse;

import java.util.List;

public interface PolicyService {
    List<PolicyResponse> getAllPolicies();
    PolicyResponse getPolicyById(int id);
    PolicyResponse createPolicy(PolicyRequest request);
    PolicyResponse updatePolicy(int id, PolicyRequest request);
    void deletePolicy(int id);
}
