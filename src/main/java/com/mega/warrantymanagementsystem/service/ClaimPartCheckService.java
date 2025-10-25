package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.ClaimPartCheck;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ClaimPartCheckRequest;
import com.mega.warrantymanagementsystem.model.response.ClaimPartCheckResponse;
import com.mega.warrantymanagementsystem.repository.ClaimPartCheckRepository;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimPartCheckService {

    @Autowired
    private ClaimPartCheckRepository claimPartCheckRepository;

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Tạo mới ClaimPartCheck
    public ClaimPartCheckResponse create(ClaimPartCheckRequest request) {
        if (claimPartCheckRepository.existsById(request.getPartNumber())) {
            throw new DuplicateResourceException("PartNumber đã tồn tại: " + request.getPartNumber());
        }

        WarrantyClaim warrantyClaim = warrantyClaimRepository.findById(request.getWarrantyId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim: " + request.getWarrantyId()));

        Vehicle vehicle = vehicleRepository.findById(request.getVin())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Vehicle: " + request.getVin()));

        ClaimPartCheck claimPartCheck = new ClaimPartCheck();
        claimPartCheck.setPartNumber(request.getPartNumber());
        claimPartCheck.setWarrantyClaim(warrantyClaim);
        claimPartCheck.setVehicle(vehicle);
        claimPartCheck.setQuantity(request.getQuantity());
        claimPartCheck.setRepair(request.isRepair());
        claimPartCheck.setPartSerial(request.getPartSerial());

        ClaimPartCheck saved = claimPartCheckRepository.save(claimPartCheck);
        return modelMapper.map(saved, ClaimPartCheckResponse.class);
    }

    // Cập nhật ClaimPartCheck
    public ClaimPartCheckResponse update(String partNumber, ClaimPartCheckRequest request) {
        ClaimPartCheck existing = claimPartCheckRepository.findById(partNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ClaimPartCheck: " + partNumber));

        if (request.getWarrantyId() != null) {
            WarrantyClaim warrantyClaim = warrantyClaimRepository.findById(request.getWarrantyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim: " + request.getWarrantyId()));
            existing.setWarrantyClaim(warrantyClaim);
        }

        if (request.getVin() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVin())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Vehicle: " + request.getVin()));
            existing.setVehicle(vehicle);
        }

        existing.setQuantity(request.getQuantity());
        existing.setRepair(request.isRepair());
        existing.setPartSerial(request.getPartSerial());

        ClaimPartCheck updated = claimPartCheckRepository.save(existing);
        return modelMapper.map(updated, ClaimPartCheckResponse.class);
    }

    // Xóa ClaimPartCheck
    public void delete(String partNumber) {
        if (!claimPartCheckRepository.existsById(partNumber)) {
            throw new ResourceNotFoundException("Không tìm thấy ClaimPartCheck: " + partNumber);
        }
        claimPartCheckRepository.deleteById(partNumber);
    }

    // Lấy tất cả
    public List<ClaimPartCheckResponse> getAll() {
        return claimPartCheckRepository.findAll().stream()
                .map(c -> modelMapper.map(c, ClaimPartCheckResponse.class))
                .collect(Collectors.toList());
    }

    // Lấy theo PartNumber
    public ClaimPartCheckResponse getByPartNumber(String partNumber) {
        ClaimPartCheck claimPartCheck = claimPartCheckRepository.findById(partNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ClaimPartCheck: " + partNumber));
        return modelMapper.map(claimPartCheck, ClaimPartCheckResponse.class);
    }

    // Tìm theo VIN
    public List<ClaimPartCheckResponse> getByVin(String vin) {
        return claimPartCheckRepository.findAll().stream()
                .filter(c -> c.getVehicle() != null && vin.equals(c.getVehicle().getVin()))
                .map(c -> modelMapper.map(c, ClaimPartCheckResponse.class))
                .collect(Collectors.toList());
    }

    // Tìm theo WarrantyId
    public List<ClaimPartCheckResponse> getByWarrantyId(String warrantyId) {
        return claimPartCheckRepository.findAll().stream()
                .filter(c -> c.getWarrantyClaim() != null && warrantyId.equals(c.getWarrantyClaim().getClaimId()))
                .map(c -> modelMapper.map(c, ClaimPartCheckResponse.class))
                .collect(Collectors.toList());
    }
}
