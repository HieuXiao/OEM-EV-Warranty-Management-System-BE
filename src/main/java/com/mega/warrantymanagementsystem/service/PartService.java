package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.PartRequest;
import com.mega.warrantymanagementsystem.model.response.*;
import com.mega.warrantymanagementsystem.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehousePartRepository warehousePartRepository;

    @Autowired
    private ModelMapper mapper;

    private static final int LOW_THRESHOLD = 50;

    @Transactional
    public PartResponse createPart(PartRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWhId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy warehouse với ID: " + request.getWhId()));

        Part part = partRepository.findById(request.getPartNumber()).orElse(null);
        if (part == null) {
            part = new Part();
            part.setPartNumber(request.getPartNumber());
            part.setNamePart(request.getNamePart());
            part.setPrice(request.getPrice());
            part = partRepository.save(part);
        }

        WarehousePartId id = new WarehousePartId(request.getPartNumber(), warehouse.getWhId());
        WarehousePart wp = warehousePartRepository.findById(id).orElse(null);

        if (wp == null) {
            wp = new WarehousePart();
            wp.setId(id);
            wp.setPart(part);
            wp.setWarehouse(warehouse);
            wp.setQuantity(request.getQuantity());
            wp.setPrice(request.getPrice());
        } else {
            wp.setQuantity(wp.getQuantity() + request.getQuantity());
        }

        warehousePartRepository.save(wp);

        // Cập nhật lowPart ngay lập tức
        updateLowPartStatus(warehouse, wp);

        return toResponse(part);
    }

    public List<PartResponse> getAllParts() {
        return partRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PartResponse getPartBySerial(String serial) {
        Part part = partRepository.findById(serial)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy part: " + serial));
        return toResponse(part);
    }

    @Transactional
    public PartResponse updatePart(String serial, PartRequest request) {
        Part part = partRepository.findById(serial)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy part: " + serial));

        // Cập nhật thông tin cơ bản
        part.setNamePart(request.getNamePart());
        part.setPrice(request.getPrice());
        partRepository.save(part);

        // Cập nhật quantity trong warehouse tương ứng (nếu có)
        if (request.getWhId() != null && request.getQuantity() >= 0) {
            Warehouse warehouse = warehouseRepository.findById(request.getWhId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy warehouse ID: " + request.getWhId()));

            WarehousePartId id = new WarehousePartId(serial, warehouse.getWhId());
            WarehousePart wp = warehousePartRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy part " + serial + " trong warehouse " + request.getWhId()));

            // Cập nhật quantity
            wp.setQuantity(request.getQuantity());
            warehousePartRepository.save(wp);

            // Đồng bộ trạng thái lowPart của warehouse
            updateLowPartStatus(warehouse, wp);
        }

        return toResponse(part);
    }


    @Transactional
    public void deletePart(String serial) {
        List<WarehousePart> list = warehousePartRepository.findByPart_PartNumber(serial);
        warehousePartRepository.deleteAll(list);
        partRepository.deleteById(serial);
    }

    private PartResponse toResponse(Part part) {
        PartResponse resp = mapper.map(part, PartResponse.class);
        List<WarehousePart> list = warehousePartRepository.findByPart_PartNumber(part.getPartNumber());
        int total = list.stream().mapToInt(WarehousePart::getQuantity).sum();
        resp.setTotalQuantity(total);

        List<WarehouseResponse> warehouses = list.stream().map(wp -> {
            WarehouseResponse wres = mapper.map(wp.getWarehouse(), WarehouseResponse.class);
            PartInWarehouseDto dto = new PartInWarehouseDto();
            dto.setPartNumber(part.getPartNumber());
            dto.setNamePart(part.getNamePart());
            dto.setQuantity(wp.getQuantity());
            dto.setPrice(wp.getPrice());
            wres.setParts(new ArrayList<>(List.of(dto)));
            return wres;
        }).collect(Collectors.toList());

        resp.setWarehouses(warehouses);
        return resp;
    }

    /**
     * Cập nhật lowPart khi có thay đổi quantity:
     * - Nếu <= 50 → thêm vào danh sách lowPart (nếu chưa có)
     * - Nếu > 50 → xóa khỏi danh sách lowPart (nếu đang có)
     */
    public void updateLowPartStatus(Warehouse warehouse, WarehousePart wp) {
        List<String> lowParts = new ArrayList<>(Optional.ofNullable(warehouse.getLowPart()).orElse(new ArrayList<>()));
        String partNumber = wp.getPart().getPartNumber();

        if (wp.getQuantity() <= LOW_THRESHOLD) {
            if (!lowParts.contains(partNumber)) {
                lowParts.add(partNumber);
            }
        } else {
            lowParts.remove(partNumber);
        }

        warehouse.setLowPart(lowParts);
        warehouseRepository.save(warehouse);
    }
}
