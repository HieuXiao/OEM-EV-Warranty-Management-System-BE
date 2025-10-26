package com.mega.warrantymanagementsystem.model.request;

import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountByRoleRequest {

    @NotEmpty(message = "Username cannot be empty!")
    private String username;

    @NotEmpty(message = "Password cannot be empty!")
    private String password;

    @NotEmpty(message = "Full name cannot be empty!")
    private String fullName;

    private Boolean gender;

    @Email
    private String email;

    @NotEmpty(message = "Phone cannot be empty!")
    private String phone;

    @NotNull(message = "Role name cannot be null!")
    private RoleName roleName; // Chọn role từ enum: ADMIN, SC_STAFF, SC_TECHNICIAN, EVM_STAFF
}
