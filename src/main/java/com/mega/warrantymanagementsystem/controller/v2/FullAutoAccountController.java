package com.mega.warrantymanagementsystem.controller.v2;

import com.mega.warrantymanagementsystem.model.request.FullAutoAccountRequest;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import com.mega.warrantymanagementsystem.service.v2.FullAutoAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/full-auto-accounts")
@CrossOrigin
@SecurityRequirement(name = "api")
public class FullAutoAccountController {

    @Autowired
    private FullAutoAccountService fullAutoAccountService;

    /**
     * API tạo account hoàn toàn tự động:
     * - Sinh accountId theo RoleName (hole-filling)
     * - Sinh username theo fullName (viết tắt + hậu tố số nếu trùng)
     */
    @PostMapping("/register")
    public ResponseEntity<AccountResponse> registerFullAuto(@RequestBody FullAutoAccountRequest request) {
        return ResponseEntity.ok(fullAutoAccountService.registerFullAuto(request));
    }
}
