package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

import java.util.List;

/**
 * Response returned when querying a warehouse.
 * parts = list of PartInWarehouseDto (quantity is per this warehouse).
 * lowPart = list of partNumbers flagged as low in this warehouse (kept separate).
 */
@Data
public class WarehouseResponse {
    private int whId;
    private String name;
    private String location;
    private List<String> lowPart;
    private List<PartInWarehouseDto> parts;
}
