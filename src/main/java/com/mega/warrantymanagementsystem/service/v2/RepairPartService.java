package com.mega.warrantymanagementsystem.service.v2;

import com.mega.warrantymanagementsystem.entity.ClaimPartCheck;
import com.mega.warrantymanagementsystem.entity.Part;
import com.mega.warrantymanagementsystem.entity.Warehouse;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.response.PartResponse;
import com.mega.warrantymanagementsystem.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairPartService {

    @Autowired
    private ClaimPartCheckRepository claimPartCheckRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    // ==================== TRỪ PART KHI CLAIM REPAIR ====================
    @Transactional
    public void handleRepairParts(String warrantyId) {
        List<ClaimPartCheck> repairParts = claimPartCheckRepository.findAll().stream()
                .filter(c -> c.getWarrantyClaim() != null && warrantyId.equals(c.getWarrantyClaim().getClaimId()))
                .filter(c -> Boolean.TRUE.equals(c.getIsRepair()))
                .collect(Collectors.toList());

        for (ClaimPartCheck check : repairParts) {
            Part part = partRepository.findByPartNumber(check.getPartNumber());
            if (part == null) {
                System.out.println("Không tìm thấy Part với PartNumber: " + check.getPartNumber());
                continue;
            }

            int newQty = part.getQuantity() - check.getQuantity();
            part.setQuantity(Math.max(newQty, 0));
            partRepository.save(part);

            syncLowParts(part.getWarehouse());
        }
    }

    // ==================== BỔ SUNG SỐ LƯỢNG PART (CÓ WAREHOUSE) ====================
    @Transactional
    public PartResponse addQuantity(String partNumber, int quantity, int warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy warehouse với ID: " + warehouseId));

        Part part = partRepository.findAll().stream()
                .filter(p -> p.getPartNumber().equals(partNumber) &&
                        p.getWarehouse() != null &&
                        p.getWarehouse().getWhId() == warehouseId)
                .findFirst()
                .orElse(null);

        if (part == null) {
            // Nếu chưa tồn tại part đó trong warehouse này → tạo mới
            part = new Part();
            part.setPartNumber(partNumber);
            part.setNamePart(partNumber);
            part.setQuantity(quantity);
            part.setPrice(0);
            part.setWarehouse(warehouse);
            partRepository.save(part);

            // Nếu nhỏ hơn hoặc bằng 50 thì thêm luôn vào danh sách lowPart của warehouse
            if (quantity <= 50) {
                List<String> lowParts = warehouse.getLowPart();
                if (lowParts == null) {
                    lowParts = new java.util.ArrayList<>();
                }
                if (!lowParts.contains(partNumber)) {
                    lowParts.add(partNumber);
                    warehouse.setLowPart(lowParts);
                    warehouseRepository.save(warehouse);
                }
            }
        } else {
            // Nếu part đã tồn tại → cộng thêm số lượng
            part.setQuantity(part.getQuantity() + quantity);
            partRepository.save(part);

            // Cập nhật lại danh sách low part cho warehouse này
            syncLowParts(warehouse);
        }

        PartResponse response = new PartResponse();
        response.setPartNumber(part.getPartNumber());
        response.setNamePart(part.getNamePart());
        response.setQuantity(part.getQuantity());
        response.setPrice(part.getPrice());
        return response;
    }

    // ==================== ĐỒNG BỘ LOW PART CHO KHO ====================
    private void syncLowParts(Warehouse warehouse) {
        if (warehouse == null) return;

        List<Part> parts = warehouse.getParts();
        if (parts == null) parts = List.of();

        List<String> updatedLowParts = parts.stream()
                .filter(p -> p.getQuantity() <= 50)
                .map(Part::getPartNumber)
                .toList();

        warehouse.setLowPart(new java.util.ArrayList<>(updatedLowParts));
        warehouseRepository.save(warehouse);
    }

}
