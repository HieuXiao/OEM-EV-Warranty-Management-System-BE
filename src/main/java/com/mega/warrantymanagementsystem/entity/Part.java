package com.mega.warrantymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "part")
@AllArgsConstructor
@NoArgsConstructor
public class Part {

    //------------------Primary Key------------------------
    @Id
    @Column(name = "part_number", length = 20)
    @NotEmpty(message = "Part serial cannot be empty!")
    private String partNumber;

    //------------------Tên part------------------------
    @Column(name = "name_part", nullable = false, length = 100)
    @NotEmpty(message = "Part name cannot be empty!")
    private String namePart;

    //------------------Số lượng------------------------
    @Column(name = "quantity")
    private int quantity;

    //------------------Giá------------------------
    @Column(name = "price")
    private float price;

    //------------------Liên kết warehouse------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "whId", nullable = false)
    @JsonIgnore
    private Warehouse warehouse;
}
