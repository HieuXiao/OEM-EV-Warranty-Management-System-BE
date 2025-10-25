package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Part;
import com.mega.warrantymanagementsystem.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, String> {

    Part findByPartSerial(String partSerial);

    List<Part> findByNamePart(String namePart);

    List<Part> findByWarehouse(Warehouse warehouse);

    boolean existsByPartSerial(String partSerial);
}

