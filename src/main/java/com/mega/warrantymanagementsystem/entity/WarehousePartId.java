package com.mega.warrantymanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key for WarehousePart: (partNumber, whId)
 * This is a separate class (Embeddable) used by WarehousePart entity.
 */
@Embeddable
@Data
public class WarehousePartId implements Serializable {

    @Column(name = "part_number", length = 50)
    private String partNumber;

    @Column(name = "wh_id")
    private Integer whId;

    public WarehousePartId() {}

    public WarehousePartId(String partNumber, Integer whId) {
        this.partNumber = partNumber;
        this.whId = whId;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Integer getWhId() {
        return whId;
    }

    public void setWhId(Integer whId) {
        this.whId = whId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WarehousePartId that = (WarehousePartId) o;
        return Objects.equals(partNumber, that.partNumber) &&
                Objects.equals(whId, that.whId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partNumber, whId);
    }
}
