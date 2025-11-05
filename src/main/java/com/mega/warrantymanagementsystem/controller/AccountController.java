package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.ChangePasswordRequest;
import com.mega.warrantymanagementsystem.model.request.UpdateRequest;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import com.mega.warrantymanagementsystem.service.AccountService;
import com.mega.warrantymanagementsystem.service.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//biểu thị đây là controller
@RequestMapping("/api/accounts")//đường dẫn chung
@CrossOrigin//cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")//yêu cầu bảo mật cho tất cả các endpoint trong controller này
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAccounts());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable String accountId) {
        AccountResponse account = accountService.findByAccountId(accountId);
        if (account == null)
            throw new ResourceNotFoundException("Account not found with ID: " + accountId);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable String accountId,
            @RequestBody UpdateRequest updateRequest) {
        return ResponseEntity.ok(accountService.updateAccount(accountId, updateRequest));
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable String accountId,
            @RequestParam boolean enabled) {
        return ResponseEntity.ok(accountService.updateAccountStatus(accountId, enabled));
    }
    @GetMapping("/current")
    public ResponseEntity<AccountResponse> getCurrentAccount() {
        return ResponseEntity.ok(accountService.getCurrentAccount());
    }

    @PostMapping("/assign-service-center/{accountId}/{centerId}")
    public ResponseEntity<String> assignServiceCenterToAccount(
            @PathVariable String accountId,
            @PathVariable int centerId) {

        String message = accountService.assignServiceCenterToAccount(accountId, centerId);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{accountId}/change-service-center/{newCenterId}")
    public ResponseEntity<String> changeServiceCenter(
            @PathVariable String accountId,
            @PathVariable int newCenterId) {
        String result = accountService.changeServiceCenterForAccount(accountId, newCenterId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{accountId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable String accountId,
            @RequestBody ChangePasswordRequest request) {

        String result = accountService.updatePassword(accountId, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok(result);
    }

}
