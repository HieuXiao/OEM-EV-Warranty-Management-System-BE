package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AccountRequest {

    @Pattern(regexp = "^(AD|ST|SS|ES)[0-9]{6}$")
    private String accountId;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String fullName;

    @NotNull
    private Boolean gender;

    @Email
    private String email;

    @NotEmpty
    private String phone;

    // RoleName enum dưới dạng String (ADMIN, SC_STAFF, SC_TECHNICIAN, EVM_STAFF)
    @NotEmpty
    private String roleName;

    private Integer serviceCenterId;

    private boolean enabled = true;
}
