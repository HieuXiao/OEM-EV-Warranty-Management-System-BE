package com.mega.warrantymanagementsystem.model.request;

import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Request cho API tự động sinh accountId & username.
 * Người dùng chỉ cần nhập thông tin cơ bản + Role.
 */
@Data
public class FullAutoAccountRequest {

    @NotEmpty(message = "Full name cannot be empty!")
    private String fullName;

    private Boolean gender;

    @Email
    private String email;

    @NotEmpty(message = "Phone cannot be empty!")
    private String phone;

    @NotEmpty(message = "Password cannot be empty!")
    private String password;

    private RoleName roleName; // ADMIN, SC_STAFF, SC_TECHNICIAN, EVM_STAFF
}
