package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.ClaimReplacementPart;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ClaimReplacementPartRequest;
import com.mega.warrantymanagementsystem.model.response.ClaimReplacementPartResponse;
import com.mega.warrantymanagementsystem.repository.ClaimReplacementPartRepository;
import com.mega.warrantymanagementsystem.service.ClaimReplacementPartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClaimReplacementPartServiceImpl implements ClaimReplacementPartService {

    @Autowired
    ClaimReplacementPartRepository claimReplacementPartRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ClaimReplacementPartResponse findById(int id) {
        Optional<ClaimReplacementPart> optional = claimReplacementPartRepository.findById(id);
        if(optional.isEmpty()){
            throw new ResourceNotFoundException("ClaimReplacementPart not found with id: " + id);
        }
        ClaimReplacementPart part = optional.get();
        return modelMapper.map(part, ClaimReplacementPartResponse.class);
    }

    @Override
    public List<ClaimReplacementPartResponse> findAll() {
        List<ClaimReplacementPart> claimReplacementParts = claimReplacementPartRepository.findAll();
        List<ClaimReplacementPartResponse> claimReplacementPartResponses = new ArrayList<>();
        for(ClaimReplacementPart c : claimReplacementParts){
            claimReplacementPartResponses.add(modelMapper.map(c,ClaimReplacementPartResponse.class));
        }
        return claimReplacementPartResponses;
    }

    @Override
    public ClaimReplacementPartResponse createClaimReplacementPart(
            ClaimReplacementPartRequest claimReplacementPartRequest) {
        Optional<ClaimReplacementPart> existing = claimReplacementPartRepository.findById(claimReplacementPartRequest.getPartUserId());
        if(existing.isPresent()){
            throw new DuplicateResourceException(
                    "Claim Replacement Part with id already exists: " + claimReplacementPartRequest.getPartUserId());
        }
        ClaimReplacementPart claimReplacementPart = modelMapper.map(
                claimReplacementPartRequest, ClaimReplacementPart.class);
        ClaimReplacementPart saved = claimReplacementPartRepository.save(claimReplacementPart);
        return modelMapper.map(saved, ClaimReplacementPartResponse.class);
    }

    @Override
    public void deleteClaimReplacementPart(int id) {
        Optional<ClaimReplacementPart> optional = claimReplacementPartRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("ClaimReplacementPart not found with id: " + id);
        }
        claimReplacementPartRepository.delete(optional.get());
    }
}
