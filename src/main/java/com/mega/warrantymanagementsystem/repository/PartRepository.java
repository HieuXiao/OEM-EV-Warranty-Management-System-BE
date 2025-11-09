package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, String> {
    Part findByPartNumber(String partNumber);
    boolean existsByPartNumber(String partNumber);
}
