package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

import java.util.List;

/**
 * Response for a global Part.
 * totalQuantity = sum of quantities across all warehouses.
 * warehouses = list of WarehouseResponse-lite showing where this part exists and its per-warehouse quantity.
 */
@Data
public class PartResponse {
    private String partNumber;
    private String namePart;
    private int totalQuantity;
    private Float price;
    private List<WarehouseResponse> warehouses;
}
