package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.ClaimPartSerial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimPartSerialRepository extends JpaRepository<ClaimPartSerial, Long> {
    boolean existsBySerialCode(String serialCode);
}
