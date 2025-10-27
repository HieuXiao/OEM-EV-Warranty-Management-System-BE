package com.mega.warrantymanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor   // cần cho Jackson
@AllArgsConstructor
public class CustomerResponse {
    private int customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private ServiceCenterResponse serviceCenter;
//    private List<VehicleResponse> vehicles;
}
