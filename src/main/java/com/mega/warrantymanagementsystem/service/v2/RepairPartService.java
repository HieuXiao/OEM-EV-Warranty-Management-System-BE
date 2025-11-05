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

            syncLowParts(part.getWarehouse()); // <-- thêm dòng này
        }
    }


    // ==================== BỔ SUNG SỐ LƯỢNG PART ====================
    @Transactional
    public PartResponse addQuantity(String partNumber, int quantity) {
        Part part = partRepository.findByPartNumber(partNumber);

        if (part == null) {
            // nếu chưa có → tạo mới luôn
            Warehouse warehouse = warehouseRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy kho nào trong hệ thống"));

            part = new Part();
            part.setPartNumber(partNumber);
            part.setNamePart(partNumber);
            part.setQuantity(quantity);
            part.setPrice(0);
            part.setWarehouse(warehouse);
            partRepository.save(part);
        } else {
            part.setQuantity(part.getQuantity() + quantity);
            partRepository.save(part);
        }

        // Sau khi bổ sung quantity, nếu > 50 thì loại khỏi danh sách lowPart
        Warehouse warehouse = part.getWarehouse();
        if (warehouse != null) {
            List<String> lowParts = warehouse.getLowPart();
            if (lowParts != null && !lowParts.isEmpty()) {
                if (part.getQuantity() > 50 && lowParts.contains(part.getPartNumber())) {
                    lowParts.remove(part.getPartNumber());
                    warehouse.setLowPart(lowParts);
                    warehouseRepository.save(warehouse);
                }
            }
        }


        PartResponse response = new PartResponse();
        response.setPartNumber(part.getPartNumber());
        response.setNamePart(part.getNamePart());
        response.setQuantity(part.getQuantity());
        response.setPrice(part.getPrice());
        return response;
    }
    private void syncLowParts(Warehouse warehouse) {
        if (warehouse == null) return;

        List<String> updatedLowParts = warehouse.getParts().stream()
                .filter(p -> p.getQuantity() <= 50)
                .map(Part::getPartNumber)
                .toList();

        warehouse.setLowPart(new java.util.ArrayList<>(updatedLowParts));
        warehouseRepository.save(warehouse);
    }

}
