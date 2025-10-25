package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;
import java.util.List;

@Data
public class WarehouseResponse {
    private int whId;
    private String name;
    private String location;
    private List<String> lowPart;
}
