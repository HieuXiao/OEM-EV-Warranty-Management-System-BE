package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.model.request.ClaimReplacementPartRequest;
import com.mega.warrantymanagementsystem.model.response.ClaimReplacementPartResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClaimReplacementPartService {

    ClaimReplacementPartResponse findById(int id);

    List<ClaimReplacementPartResponse> findAll();

    ClaimReplacementPartResponse createClaimReplacementPart(ClaimReplacementPartRequest claimReplacementPartRequest);
    void deleteClaimReplacementPart(int id);

}
