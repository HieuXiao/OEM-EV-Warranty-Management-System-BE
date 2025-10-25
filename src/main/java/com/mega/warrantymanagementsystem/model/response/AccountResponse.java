package com.mega.warrantymanagementsystem.model.response;

import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import lombok.Data;

@Data
public class AccountResponse {
    private String accountId;
    private String username;
    private String fullName;
    private Boolean gender;
    private String email;
    private String phone;
    private boolean enabled;
    private RoleName roleName;
    private ServiceCenterResponse serviceCenter;
}
