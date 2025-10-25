package com.mega.warrantymanagementsystem.model.response;

import lombok.Data;
import java.util.List;

@Data
public class CustomerResponse {
    private int customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private List<VehicleResponse> vehicles;
}
