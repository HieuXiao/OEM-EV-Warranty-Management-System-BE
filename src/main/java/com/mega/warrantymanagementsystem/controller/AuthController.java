package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.model.request.AccountRequest;
import com.mega.warrantymanagementsystem.model.request.LoginRequest;
import com.mega.warrantymanagementsystem.model.request.UpdatePasswordRequest;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import com.mega.warrantymanagementsystem.service.AccountService;
import com.mega.warrantymanagementsystem.service.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController//biểu thị đây là controller
@RequestMapping("/api/auth")//đường dẫn chung
@CrossOrigin//cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")//yêu cầu bảo mật cho tất cả các endpoint trong controller này
public class AuthController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ======================= REGISTER =======================
    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@Valid @RequestBody AccountRequest accoutRequest) {
        // Kiểm tra trùng accountId
        try {
            accountService.findByAccountId(accoutRequest.getAccountId());
            throw new DuplicateResourceException("Account ID " + accoutRequest.getAccountId() + " already exists");
        } catch (Exception ignored) { } // nếu ném ResourceNotFound thì pass

        // Kiểm tra trùng username
        try {
            accountService.findByUsername(accoutRequest.getUsername());
            throw new DuplicateResourceException("Username " + accoutRequest.getUsername() + " already exists");
        } catch (Exception ignored) { }

        // Kiểm tra trùng email
        try {
            accountService.findByEmail(accoutRequest.getEmail());
            throw new DuplicateResourceException("Email " + accoutRequest.getEmail() + " already exists");
        } catch (Exception ignored) { }

        AccountResponse createdAccount = accountService.createAccount(accoutRequest);
        return ResponseEntity.ok(createdAccount);
    }

    // ======================= LOGIN =======================
    @PostMapping("/login")
    public ResponseEntity<AccountResponse> login(@RequestBody LoginRequest loginRequest) {
        AccountResponse account = accountService.login(loginRequest);
        return ResponseEntity.ok(account);
    }

    // đăng kí forgot password
    // B1: gửi một cái mail xác nhận
    // link update passwork => không ai cũng dùng được

    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String email){
        accountService.resetPassword(email);
    }

    //update password
    @PostMapping("/update-password")
    public ResponseEntity<AccountResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        AccountResponse response = accountService.updateForgotPassword(updatePasswordRequest);
        return ResponseEntity.ok(response);
    }

}
