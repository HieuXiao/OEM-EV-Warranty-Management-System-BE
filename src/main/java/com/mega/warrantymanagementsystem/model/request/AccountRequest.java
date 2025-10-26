package com.mega.warrantymanagementsystem.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {
    @NotEmpty(message = "ID cannot be empty!")//không được để trống
    private String accountId;
    @NotEmpty(message = "Username cannot be empty!")
    private String username;
    @NotEmpty(message = "Password cannot be empty!")
    private String password;
    @NotEmpty(message = "Full name cannot be empty!")
    private String fullName;
    //true = Male, false = Female
    private Boolean gender;
    @Email
    private String email;
    @NotEmpty(message = "phone cannot be empty!")
    private String phone;
}
