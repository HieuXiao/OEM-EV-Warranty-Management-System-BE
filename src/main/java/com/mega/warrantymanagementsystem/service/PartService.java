package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Part;
import com.mega.warrantymanagementsystem.model.request.PartRequest;
import com.mega.warrantymanagementsystem.model.response.PartResponse;

import java.util.List;

public interface PartService {
    Part createPart(PartRequest partRequest);
    Part updatePart(int partId, PartRequest partRequest);
    void deletePart(int partId);
    PartResponse getPartById(int partId);
    List<PartResponse> getAllParts();
}
