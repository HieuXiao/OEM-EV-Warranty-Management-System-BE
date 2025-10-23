package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // tự động sinh constructor có tham số
@NoArgsConstructor  // tự động sinh constructor không tham số
public class PartResponse {
    private int partId;
    private String name;
    private String serialNumber;
    private String description;
    private int inventoryId;
}
