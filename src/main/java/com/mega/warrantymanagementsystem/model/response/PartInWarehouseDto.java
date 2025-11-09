package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

/**
 * Small DTO representing a part inside a specific warehouse (used in WarehouseResponse. parts and in WarehouseResponse list inside PartResponse).
 */
@Data
public class PartInWarehouseDto {
    private String partNumber;
    private String namePart;
    private int quantity;
    private Float price;
}
