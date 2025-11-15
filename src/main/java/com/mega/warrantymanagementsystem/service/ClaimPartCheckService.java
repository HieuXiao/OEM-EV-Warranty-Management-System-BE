package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ClaimPartCheckRequest;
import com.mega.warrantymanagementsystem.model.response.ClaimPartCheckResponse;
import com.mega.warrantymanagementsystem.model.response.VehicleResponse;
import com.mega.warrantymanagementsystem.repository.ClaimPartCheckRepository;
import com.mega.warrantymanagementsystem.repository.PartUnderWarrantyRepository;
import com.mega.warrantymanagementsystem.repository.VehicleRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import jakarta.transaction.Transactional;
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
    private PartUnderWarrantyRepository partUnderWarrantyRepository;


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

        if (request.getPartId() != null && !request.getPartId().isEmpty()) {
            PartUnderWarranty part = partUnderWarrantyRepository.findById(request.getPartId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy PartUnderWarranty: " + request.getPartId()));
            claimPartCheck.setPartUnderWarranty(part);
        } else {
            claimPartCheck.setPartUnderWarranty(null);
        }

        ClaimPartCheck saved = claimPartCheckRepository.save(claimPartCheck);
        return toResponse(saved);
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
        return toResponse(updated);
    }

    // ================= ADD SERIALS =================
    @Transactional
    public ClaimPartCheckResponse addPartSerials(String claimId, String partNumber, List<String> serialCodes) {
        ClaimPartCheck existing = claimPartCheckRepository.findByPartNumberAndWarrantyClaim_ClaimId(partNumber, claimId);
        if (existing == null) {
            throw new ResourceNotFoundException("Không tìm thấy partNumber " + partNumber + " trong claim " + claimId);
        }

        if (existing.getIsRepair() == null || !existing.getIsRepair()) {
            throw new BusinessLogicException("Bộ phận này không được đánh dấu là cần sửa (isRepair=false).");
        }

        if (serialCodes == null || serialCodes.isEmpty()) {
            throw new BusinessLogicException("Danh sách partSerial không được để trống.");
        }

        if (existing.getQuantity() != serialCodes.size()) {
            throw new BusinessLogicException("Số lượng partSerial phải đúng bằng quantity (" + existing.getQuantity() + ").");
        }

        // kiểm tra trùng trong cùng claimPartCheck
        List<String> currentCodes = existing.getPartSerials().stream()
                .map(ClaimPartSerial::getSerialCode)
                .toList();

        for (String code : serialCodes) {
            if (currentCodes.contains(code)) {
                throw new DuplicateResourceException("PartSerial trùng trong cùng ClaimPartCheck: " + code);
            }
        }

        // clear và thêm mới
        existing.getPartSerials().clear();
        for (String code : serialCodes) {
            ClaimPartSerial serial = new ClaimPartSerial();
            serial.setSerialCode(code);
            serial.setClaimPartCheck(existing);
            existing.getPartSerials().add(serial);
        }

        ClaimPartCheck saved = claimPartCheckRepository.save(existing);
        return toResponse(saved);
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
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ClaimPartCheckResponse getByPartNumber(String claimId, String partNumber) {
        ClaimPartCheck claimPartCheck = claimPartCheckRepository.findByPartNumberAndWarrantyClaim_ClaimId(partNumber, claimId);
        if (claimPartCheck == null) {
            throw new ResourceNotFoundException("Không tìm thấy partNumber " + partNumber +
                    " trong claim " + claimId);
        }
        return toResponse(claimPartCheck);
    }

    public List<ClaimPartCheckResponse> getByVin(String vin) {
        return claimPartCheckRepository.findAll().stream()
                .filter(c -> c.getVehicle() != null && vin.equals(c.getVehicle().getVin()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ClaimPartCheckResponse> getByWarrantyId(String warrantyId) {
        return claimPartCheckRepository.findAll().stream()
                .filter(c -> c.getWarrantyClaim() != null && warrantyId.equals(c.getWarrantyClaim().getClaimId()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= PRIVATE MAPPER =================
    private ClaimPartCheckResponse toResponse(ClaimPartCheck entity) {
        ClaimPartCheckResponse res = new ClaimPartCheckResponse();
        res.setPartNumber(entity.getPartNumber());
        res.setQuantity(entity.getQuantity());
        res.setIsRepair(entity.getIsRepair());

        if (entity.getPartSerials() != null) {
            res.setPartSerials(entity.getPartSerials().stream()
                    .map(ClaimPartSerial::getSerialCode)
                    .collect(Collectors.toList()));
        }

        if (entity.getVehicle() != null) {
            VehicleResponse v = new VehicleResponse();
            v.setVin(entity.getVehicle().getVin());
            v.setModel(entity.getVehicle().getModel());
            res.setVehicle(v);
        }

        return res;
    }
    //====================DELETE CLAIM PART CHECK BY WARRANTY CLAIM=====================
    @Transactional
    public void deleteAllByClaim(String claimId) {

        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy WarrantyClaim: " + claimId));

        if (claim.getStatus() != WarrantyClaimStatus.CHECK) {
            throw new BusinessLogicException("Chỉ được xóa ClaimPartCheck khi claim đang ở trạng thái CHECK.");
        }

        claimPartCheckRepository.deleteByWarrantyClaim_ClaimId(claimId);
    }

}
