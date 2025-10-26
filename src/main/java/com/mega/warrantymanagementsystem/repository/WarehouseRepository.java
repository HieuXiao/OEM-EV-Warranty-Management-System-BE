package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

    // Tìm warehouse theo tên
    Warehouse findByName(String name);

    // Tìm danh sách warehouse theo location
    List<Warehouse> findByLocation(String location);

    boolean existsByNameIgnoreCase(String name);
    boolean existsByLocationIgnoreCase(String location);
}
