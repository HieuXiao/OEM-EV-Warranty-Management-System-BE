package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

@Data
public class PartResponse {
    private String partNumber;
    private String namePart;
    private int quantity;
    private float price;
    private WarehouseResponse warehouse;
}
