package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.*;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.WarehouseRequest;
import com.mega.warrantymanagementsystem.model.response.*;
import com.mega.warrantymanagementsystem.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehousePartRepository warehousePartRepository;

    @Autowired
    private PartUnderWarrantyRepository partUnderWarrantyRepository;

    @Autowired
    private ModelMapper mapper;

    public WarehouseResponse create(WarehouseRequest req) {
        Warehouse wh = mapper.map(req, Warehouse.class);
        return mapper.map(warehouseRepository.save(wh), WarehouseResponse.class);
    }

    public WarehouseResponse update(Integer id, WarehouseRequest req) {
        Warehouse wh = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy warehouse ID: " + id));
        wh.setName(req.getName());
        wh.setLocation(req.getLocation());
        return mapper.map(warehouseRepository.save(wh), WarehouseResponse.class);
    }

    @Transactional
    public void delete(Integer id) {
        Warehouse wh = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy warehouse ID: " + id));
        List<WarehousePart> parts = warehousePartRepository.findByWarehouse_WhId(id);
        warehousePartRepository.deleteAll(parts);
        warehouseRepository.delete(wh);
    }

    public List<WarehouseResponse> getAll() {
        return warehouseRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WarehouseResponse getById(Integer id) {
        Warehouse wh = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy warehouse ID: " + id));
        return toResponse(wh);
    }

    public List<WarehouseResponse> getByName(String name) {
        return warehouseRepository.findAll().stream()
                .filter(w -> w.getName().toLowerCase().contains(name.toLowerCase()))
                .map(this::toResponse)
                .toList();
    }

    public List<WarehouseResponse> getByLocation(String location) {
        return warehouseRepository.findAll().stream()
                .filter(w -> w.getLocation().toLowerCase().contains(location.toLowerCase()))
                .map(this::toResponse)
                .toList();
    }

    private WarehouseResponse toResponse(Warehouse wh) {
        WarehouseResponse res = mapper.map(wh, WarehouseResponse.class);
        List<WarehousePart> wps = warehousePartRepository.findByWarehouse_WhId(wh.getWhId());
        List<PartInWarehouseDto> parts = wps.stream()
                .filter(wp -> {
                    Part part = wp.getPart();
                    if (part == null) return false;
                    PartUnderWarranty puw = partUnderWarrantyRepository.findById(part.getPartNumber()).orElse(null);
                    return puw != null && Boolean.TRUE.equals(puw.getIsEnable());
                })
                .map(wp -> {
                    PartInWarehouseDto dto = new PartInWarehouseDto();
                    dto.setPartNumber(wp.getPart().getPartNumber());
                    dto.setNamePart(wp.getPart().getNamePart());
                    dto.setQuantity(wp.getQuantity());
                    dto.setPrice(wp.getPrice());
                    return dto;
                })
                .collect(Collectors.toList());

        res.setParts(parts);
        return res;
    }
}
