package com.mega.warrantymanagementsystem.service.v2;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.Role;
import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.AccountByRoleRequest;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.repository.AccountRepository;
import com.mega.warrantymanagementsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tự động sinh accountId dựa theo RoleName (hole-filling).
 * Ví dụ:
 *  - ADMIN → AD000000, AD000001, ...
 *  - SC_STAFF → SS000000, SS000001, ...
 *  - ...
 *
 * Cơ chế:
 *  - Lấy tất cả account hiện có có prefix tương ứng
 *  - Lọc phần số 6 chữ số hợp lệ
 *  - Tìm số nhỏ nhất chưa dùng (từ 0..999999)
 *  - Tạo ID = PREFIX + %06d
 *
 * Lưu ý concurrency: có cơ chế retry khi save bị trùng do race condition.
 */
@Service
public class AutoAccountService {

    private static final int MAX_TRIES = 3;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Đăng ký tài khoản dựa vào RoleName (ID tự sinh bằng hole-filling).
     */
    @Transactional
    public AccountResponse registerByRole(AccountByRoleRequest request) {
        // validate unique username/email first
        if (accountRepository.findByUsername(request.getUsername()) != null) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        if (request.getEmail() != null && accountRepository.findByEmail(request.getEmail()) != null) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // determine prefix
        String prefix = switch (request.getRoleName()) {
            case ADMIN -> "AD";
            case SC_STAFF -> "SS";
            case SC_TECHNICIAN -> "ST";
            case EVM_STAFF -> "ES";
        };

        // ensure role exists
        Role role = roleRepository.findById(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRoleName()));

        // retry loop to mitigate race conditions (unique constraint at DB level must exist)
        DataIntegrityViolationException lastDIVE = null;
        for (int attempt = 1; attempt <= MAX_TRIES; attempt++) {
            String newId = findNextIdHoleFilling(prefix);

            Account acc = new Account();
            acc.setAccountId(newId);
            acc.setUsername(request.getUsername());
            acc.setPassword(passwordEncoder.encode(request.getPassword()));
            acc.setFullName(request.getFullName());
            acc.setGender(request.getGender());
            acc.setEmail(request.getEmail());
            acc.setPhone(request.getPhone());
            acc.setRole(role);
            acc.setEnabled(true);

            try {
                Account saved = accountRepository.save(acc);
                return mapToResponse(saved);
            } catch (DataIntegrityViolationException dive) {
                // có thể do race (ai đó vừa insert cùng ID), retry
                lastDIVE = dive;
                // small backoff optional (not added here) — loop will retry and recompute ID
            }
        }

        // nếu vẫn fail sau retries -> ném lỗi
        throw new RuntimeException("Unable to create account after retries.", lastDIVE);
    }

    /**
     * Tìm số nhỏ nhất chưa dùng cho prefix (hole-filling).
     * Lấy tất cả account có prefix, parse phần 6 chữ số hợp lệ và tìm gap.
     */
    private String findNextIdHoleFilling(String prefix) {
        // fetch all account IDs that start with prefix
        List<String> ids = accountRepository.findAll().stream()
                .map(Account::getAccountId)
                .filter(Objects::nonNull)
                .filter(id -> id.startsWith(prefix))
                .collect(Collectors.toList());

        // collect used numbers (only those with exactly 6 trailing digits)
        BitSet used = new BitSet(1_000_000); // 0..999999
        for (String id : ids) {
            if (id.length() == 8) { // 2 letters + 6 digits
                String numPart = id.substring(2);
                if (numPart.matches("\\d{6}")) {
                    int val = Integer.parseInt(numPart);
                    if (val >= 0 && val <= 999_999) {
                        used.set(val);
                    }
                }
            } else if (id.length() > 2) {
                // still try to be forgiving: if trailing part has digits but not exactly 6, ignore it
                String tail = id.substring(2);
                if (tail.matches("\\d+")) {
                    // ignore non-6-digit numbers to keep behavior predictable (you wanted 6-digit space)
                }
            }
        }

        // find first clear bit (first unused number)
        int next = used.nextClearBit(0);
        if (next < 0 || next > 999_999) {
            throw new IllegalStateException("ID range exhausted for prefix " + prefix);
        }
        return prefix + String.format("%06d", next);
    }

    private AccountResponse mapToResponse(Account account) {
        AccountResponse res = new AccountResponse();
        res.setAccountId(account.getAccountId());
        res.setUsername(account.getUsername());
        res.setFullName(account.getFullName());
        res.setGender(account.getGender());
        res.setEmail(account.getEmail());
        res.setPhone(account.getPhone());
        res.setEnabled(account.isEnabled());

        if (account.getRole() != null)
            res.setRoleName(account.getRole().getRoleName().name());

        if (account.getServiceCenter() != null) {
            ServiceCenterResponse sc = new ServiceCenterResponse(
                    account.getServiceCenter().getCenterId(),
                    account.getServiceCenter().getCenterName(),
                    account.getServiceCenter().getLocation()
            );
            res.setServiceCenter(sc);
        }
        return res;
    }
}
