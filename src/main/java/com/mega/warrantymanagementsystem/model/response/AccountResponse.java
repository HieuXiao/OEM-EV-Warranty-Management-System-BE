package com.mega.warrantymanagementsystem.model.response;

import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import lombok.Data;

@Data
public class AccountResponse {
    String accountId;
    String username;
    String fullName;
    Boolean gender;
    String email;
    String phone;
    String token;

    String roleName;

    private boolean enabled;

    // Gói lại thông tin Service Center
    private ServiceCenterResponse serviceCenter;
}
