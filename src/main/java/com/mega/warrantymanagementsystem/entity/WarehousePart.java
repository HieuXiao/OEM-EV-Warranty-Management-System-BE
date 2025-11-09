package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Join entity representing quantity (and optional price) of a Part in a specific Warehouse.
 * This is the core of the new n-n relationship with per-warehouse quantity.
 */
@Entity
@Table(name = "warehouse_part")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehousePart {

    @EmbeddedId
    private WarehousePartId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("partNumber")
    @JoinColumn(name = "part_number", referencedColumnName = "part_number")
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("whId")
    @JoinColumn(name = "wh_id", referencedColumnName = "wh_id")
    private Warehouse warehouse;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    // optional per-warehouse price; can be null
    @Column(name = "price")
    private Float price;
}
