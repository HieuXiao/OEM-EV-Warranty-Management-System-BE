package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.PartUnderWarranty;
import com.mega.warrantymanagementsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // đánh dấu đây là tầng repository (DAO)
public interface PartUnderWarrantyRepository extends JpaRepository<PartUnderWarranty, String> {

    // tìm part theo serial
    PartUnderWarranty findByPartSerial(String partSerial);

    // tìm các part theo tên
    List<PartUnderWarranty> findByPartName(String partName);

    // tìm các part được quản lý bởi một admin cụ thể
    List<PartUnderWarranty> findByAdmin(Account admin);

    // kiểm tra trùng serial
    boolean existsByPartSerial(String partSerial);
}
