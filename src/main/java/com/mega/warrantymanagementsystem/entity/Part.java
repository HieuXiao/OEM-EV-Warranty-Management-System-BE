package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Global Part entity. Does NOT store per-warehouse quantity.
 * Per-warehouse quantities live in WarehousePart.
 */
@Entity
@Table(name = "part")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {

    @Id
    @Column(name = "part_number", length = 50)
    private String partNumber;

    @Column(name = "name_part", nullable = false, length = 200)
    private String namePart;

    // optional default/global price
    @Column(name = "price")
    private Float price;

    // OneToMany to WarehousePart for convenience (mappedBy = "part")
    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehousePart> inventories = new ArrayList<>();
}
