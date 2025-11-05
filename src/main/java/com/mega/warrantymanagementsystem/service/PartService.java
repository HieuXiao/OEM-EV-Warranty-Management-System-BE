package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Part;
import com.mega.warrantymanagementsystem.entity.Warehouse;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.model.request.PartRequest;
import com.mega.warrantymanagementsystem.model.response.PartResponse;
import com.mega.warrantymanagementsystem.model.response.WarehouseResponse;
import com.mega.warrantymanagementsystem.repository.PartRepository;
import com.mega.warrantymanagementsystem.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    public List<PartResponse> getAllParts() {
        return partRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PartResponse getPartBySerial(String partSerial) {
        Part part = partRepository.findByPartNumber(partSerial);
        if (part == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Part not found: " + partSerial);
        return toResponse(part);
    }

    // ================= CREATE =================
    public PartResponse createPart(PartRequest request) {
        if (partRepository.existsByPartNumber(request.getPartNumber())) {
            throw new DuplicateResourceException("Part already exists: " + request.getPartNumber());
        }

        Warehouse wh = warehouseRepository.findById(request.getWhId())
                .orElseThrow(() -> new DuplicateResourceException("Warehouse not found: " + request.getWhId()));

        Part part = new Part();
        part.setPartNumber(request.getPartNumber());
        part.setNamePart(request.getNamePart());
        part.setQuantity(request.getQuantity());
        part.setPrice(request.getPrice());
        part.setWarehouse(wh);

        Part saved = partRepository.save(part);

        // Nếu quantity < 50 → thêm vào danh sách lowPart của Warehouse
        if (saved.getQuantity() < 50) {
            List<String> lowParts = wh.getLowPart();
            if (lowParts == null) lowParts = new ArrayList<>();

            if (!lowParts.contains(saved.getPartNumber())) {
                lowParts.add(saved.getPartNumber());
                wh.setLowPart(lowParts);
                warehouseRepository.save(wh);
            }
        }

        return toResponse(saved);
    }

    // ================= UPDATE =================
    public PartResponse updatePart(String partSerial, PartRequest request) {
        Part part = partRepository.findByPartNumber(partSerial);
        if (part == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Part not found: " + partSerial);

        if (request.getPartNumber() != null && !request.getPartNumber().equals(part.getPartNumber())) {
            if (partRepository.existsByPartNumber(request.getPartNumber())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Part number already exists: " + request.getPartNumber());
            }
            part.setPartNumber(request.getPartNumber());
        }

        if (request.getNamePart() != null) part.setNamePart(request.getNamePart());
        part.setQuantity(request.getQuantity());
        part.setPrice(request.getPrice());

        if (request.getWhId() != null) {
            Warehouse wh = warehouseRepository.findById(request.getWhId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Warehouse not found: " + request.getWhId()));
            part.setWarehouse(wh);
        }

        return toResponse(partRepository.save(part));
    }

    // ================= DELETE =================
    public void deletePart(String partNumber) {
        Part part = partRepository.findByPartNumber(partNumber);
        if (part == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Part not found: " + partNumber);
        partRepository.delete(part);
    }

    // ================= RESPONSE MAPPER =================
    private PartResponse toResponse(Part part) {
        PartResponse resp = new PartResponse();
        resp.setPartNumber(part.getPartNumber());
        resp.setNamePart(part.getNamePart());
        resp.setQuantity(part.getQuantity());
        resp.setPrice(part.getPrice());

        if (part.getWarehouse() != null) {
            WarehouseResponse whResp = new WarehouseResponse();
            whResp.setWhId(part.getWarehouse().getWhId());
            whResp.setName(part.getWarehouse().getName());
            whResp.setLocation(part.getWarehouse().getLocation());
            resp.setWarehouse(whResp);
        }

        return resp;
    }
}
