package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // tự động sinh constructor có tham số
@NoArgsConstructor  // tự động sinh constructor không tham số
public class InventoryResponse {
    private int inventoryId;
    private String location;
}
