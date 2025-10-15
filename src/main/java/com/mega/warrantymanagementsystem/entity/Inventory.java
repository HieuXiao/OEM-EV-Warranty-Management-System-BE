package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // đánh dấu là 1 thực thể
@Data // tự động sinh getter setter
@Table(name = "inventory") // tên bảng trong DB
@AllArgsConstructor // tự động sinh constructor có tham số
@NoArgsConstructor // tự động sinh constructor không tham số
public class Inventory {

    //------------------Id của inventory------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id", nullable = false)
    private int inventoryId;

    //------------------Địa điểm------------------------
    @Column(name = "location", nullable = false, length = 100)
    @NotEmpty(message = "Location cannot be empty!")
    private String location;
}