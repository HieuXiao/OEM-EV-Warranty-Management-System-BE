package com.mega.warrantymanagementsystem.controller.v2;

import com.mega.warrantymanagementsystem.model.request.AccountByRoleRequest;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import com.mega.warrantymanagementsystem.service.v2.AutoAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auto-accounts")
@CrossOrigin
@SecurityRequirement(name = "api")
public class AutoAccountController {

    @Autowired
    private AutoAccountService autoAccountService;

    /**
     * API đăng ký account mới dựa trên roleName, ID được tự động sinh ra.
     * Ví dụ: POST /api/auto-accounts/register-by-role
     */
    @PostMapping("/register-by-role")
    public ResponseEntity<AccountResponse> registerByRole(@RequestBody AccountByRoleRequest request) {
        return ResponseEntity.ok(autoAccountService.registerByRole(request));
    }
}
