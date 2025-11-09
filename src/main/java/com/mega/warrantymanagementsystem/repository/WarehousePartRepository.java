package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.WarehousePart;
import com.mega.warrantymanagementsystem.entity.WarehousePartId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehousePartRepository extends JpaRepository<WarehousePart, WarehousePartId> {

    List<WarehousePart> findByPart_PartNumber(String partNumber);

    List<WarehousePart> findByWarehouse_WhId(Integer whId);

    Optional<WarehousePart> findByPart_PartNumberAndWarehouse_WhId(String partNumber, Integer whId);
}
