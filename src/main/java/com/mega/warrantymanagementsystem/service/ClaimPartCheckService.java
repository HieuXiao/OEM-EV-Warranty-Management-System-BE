package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.ClaimPartCheck;
import com.mega.warrantymanagementsystem.entity.Vehicle;
import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
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

    // ================= CREATE =================
    public ClaimPartCheckResponse create(ClaimPartCheckRequest request) {
        if (claimPartCheckRepository.existsByPartNumberAndWarrantyClaim_ClaimId(request.getPartNumber(), request.getWarrantyId())) {
            throw new DuplicateResourceException("PartNumber " + request.getPartNumber() +
                    " đã tồn tại trong Claim " + request.getWarrantyId());
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
        claimPartCheck.setIsRepair(request.getIsRepair() != null ? request.getIsRepair() : false);

        ClaimPartCheck saved = claimPartCheckRepository.save(claimPartCheck);
        return modelMapper.map(saved, ClaimPartCheckResponse.class);
    }

    // ================= UPDATE =================
    public ClaimPartCheckResponse update(String claimId, String partNumber, ClaimPartCheckRequest request) {
        ClaimPartCheck existing = claimPartCheckRepository.findByPartNumberAndWarrantyClaim_ClaimId(partNumber, claimId);
        if (existing == null) {
            throw new ResourceNotFoundException("Không tìm thấy partNumber " + partNumber +
                    " trong claim " + claimId);
        }

        if (request.getQuantity() != null) {
            existing.setQuantity(request.getQuantity());
        }

        if (request.getIsRepair() != null) {
            existing.setIsRepair(request.getIsRepair());
        }

        ClaimPartCheck updated = claimPartCheckRepository.save(existing);
        return modelMapper.map(updated, ClaimPartCheckResponse.class);
    }

    // ================= ADD SERIAL =================
    public ClaimPartCheckResponse addPartSerial(String claimId, String partNumber, String partSerial) {
        ClaimPartCheck existing = claimPartCheckRepository.findByPartNumberAndWarrantyClaim_ClaimId(partNumber, claimId);
        if (existing == null) {
            throw new ResourceNotFoundException("Không tìm thấy partNumber " + partNumber +
                    " trong claim " + claimId);
        }

        WarrantyClaim claim = existing.getWarrantyClaim();
        if (claim == null) {
            throw new ResourceNotFoundException("Claim liên kết không tồn tại");
        }

        if (claim.getStatus() == null || !claim.getStatus().name().equalsIgnoreCase("REPAIR")) {
            throw new BusinessLogicException("Chỉ được thêm partSerial khi claim đang ở trạng thái REPAIR.");
        }

        if (existing.getIsRepair() == null || !existing.getIsRepair()) {
            throw new BusinessLogicException("Không thể thêm partSerial cho bộ phận không được đánh dấu là cần sửa (isRepair=false).");
        }

        existing.setPartSerial(partSerial);
        ClaimPartCheck saved = claimPartCheckRepository.save(existing);
        return modelMapper.map(saved, ClaimPartCheckResponse.class);
    }

    // ================= DELETE =================
    public void delete(String claimId, String partNumber) {
        ClaimPartCheck existing = claimPartCheckRepository.findByPartNumberAndWarrantyClaim_ClaimId(partNumber, claimId);
        if (existing == null) {
            throw new ResourceNotFoundException("Không tìm thấy partNumber " + partNumber +
                    " trong claim " + claimId);
        }
        claimPartCheckRepository.delete(existing);
    }

    // ================= GET =================
    public List<ClaimPartCheckResponse> getAll() {
        return claimPartCheckRepository.findAll().stream()
                .map(c -> modelMapper.map(c, ClaimPartCheckResponse.class))
                .collect(Collectors.toList());
    }

    public ClaimPartCheckResponse getByPartNumber(String claimId, String partNumber) {
        ClaimPartCheck claimPartCheck = claimPartCheckRepository.findByPartNumberAndWarrantyClaim_ClaimId(partNumber, claimId);
        if (claimPartCheck == null) {
            throw new ResourceNotFoundException("Không tìm thấy partNumber " + partNumber +
                    " trong claim " + claimId);
        }
        return modelMapper.map(claimPartCheck, ClaimPartCheckResponse.class);
    }

    public List<ClaimPartCheckResponse> getByVin(String vin) {
        return claimPartCheckRepository.findAll().stream()
                .filter(c -> c.getVehicle() != null && vin.equals(c.getVehicle().getVin()))
                .map(c -> modelMapper.map(c, ClaimPartCheckResponse.class))
                .collect(Collectors.toList());
    }

    public List<ClaimPartCheckResponse> getByWarrantyId(String warrantyId) {
        return claimPartCheckRepository.findAll().stream()
                .filter(c -> c.getWarrantyClaim() != null && warrantyId.equals(c.getWarrantyClaim().getClaimId()))
                .map(c -> modelMapper.map(c, ClaimPartCheckResponse.class))
                .collect(Collectors.toList());
    }
}
