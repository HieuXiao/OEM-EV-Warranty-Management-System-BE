package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse entity. It will have a list of WarehousePart entries (parts + quantities).
 * lowPart is kept as an ElementCollection of partNumbers to match your existing model.
 */
@Entity
@Table(name = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wh_id")
    private Integer whId;

    @Column(nullable = false)
    private String name;

    /**
     * location should follow a consistent format (e.g. "Country,City,Province,District")
     * so the location priority matching in handleRepairParts can work reliably.
     */
    @Column(nullable = false)
    private String location;

    /**
     * lowPart: list of partNumbers that are considered "low" in this warehouse.
     * We keep it as ElementCollection to match your existing response model.
     */
    @ElementCollection
    @CollectionTable(name = "warehouse_low_part", joinColumns = @JoinColumn(name = "wh_id"))
    @Column(name = "part_number")
    private List<String> lowPart = new ArrayList<>();

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehousePart> parts = new ArrayList<>();
}
