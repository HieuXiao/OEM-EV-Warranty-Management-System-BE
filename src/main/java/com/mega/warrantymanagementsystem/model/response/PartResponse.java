package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;

@Data
public class PartResponse {
    private String partSerial;
    private String namePart;
    private int quantity;
    private float price;
    private WarehouseResponse warehouse;
}
