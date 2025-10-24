package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parts_under_warranty")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartUnderWarranty {

    //------------------Primary Key------------------------
    @Id
    @Column(name = "part_serial", length = 20)
    private String partSerial;

    //------------------Liên kết Account (Admin quản lý part)------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", referencedColumnName = "accountId", nullable = false)
    @JsonIgnore
    private Account admin;

    //------------------Tên part------------------------
    @Column(name = "part_name", length = 100, nullable = false)
    @NotEmpty(message = "Part name cannot be empty!")
    private String partName;

    //------------------Hãng part------------------------
    @Column(name = "part_branch", length = 50)
    private String partBranch;

    //------------------Giá------------------------
    @Column(name = "price")
    private Float price;

    //------------------Model xe------------------------
    @Column(name = "vehicle_model", length = 100)
    private String vehicleModel;

    //------------------Mô tả------------------------
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}

