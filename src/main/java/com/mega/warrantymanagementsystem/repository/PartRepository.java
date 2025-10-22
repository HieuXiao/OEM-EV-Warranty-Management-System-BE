package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Inventory;
import com.mega.warrantymanagementsystem.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // đánh dấu đây là tầng repository (DAO)
public interface PartRepository extends JpaRepository<Part, Integer> {

    // tìm part theo serial number
    Part findBySerialNumber(String serialNumber);

    // tìm các part theo tên (trường hợp có nhiều part cùng tên)
    List<Part> findByName(String Name);

    // tìm các part thuộc về 1 inventory cụ thể
    List<Part> findByInventoryId(Inventory inventoryId);


    boolean existsBySerialNumber(String serialNumber);
}
