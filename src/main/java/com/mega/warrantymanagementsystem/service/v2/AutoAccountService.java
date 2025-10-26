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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Tự động sinh accountId dựa theo RoleName.
 * Ví dụ:
 *  - ADMIN → AD000000, AD000001, ...
 *  - SC_STAFF → SS000000, SS000001, ...
 *  - SC_TECHNICIAN → ST000000, ST000001, ...
 *  - EVM_STAFF → ES000000, ES000001, ...
 */
@Service
public class AutoAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Đăng ký tài khoản dựa vào RoleName (ID tự sinh).
     */
    public AccountResponse registerByRole(AccountByRoleRequest request) {
        // Kiểm tra trùng username, email
        if (accountRepository.findByUsername(request.getUsername()) != null) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        if (accountRepository.findByEmail(request.getEmail()) != null) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // Xác định prefix theo RoleName
        String prefix = switch (request.getRoleName()) {
            case ADMIN -> "AD";
            case SC_STAFF -> "SS";
            case SC_TECHNICIAN -> "ST";
            case EVM_STAFF -> "ES";
        };

        // Lấy tất cả account có prefix đó
        List<Account> existing = accountRepository.findAll().stream()
                .filter(a -> a.getAccountId().startsWith(prefix))
                .collect(Collectors.toList());

        // Tìm mã số lớn nhất
        int max = existing.stream()
                .map(a -> a.getAccountId().substring(2))
                .filter(s -> s.matches("\\d{6}"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(-1);

        int next = max + 1;
        if (next > 999999)
            throw new IllegalStateException("ID range exhausted for prefix " + prefix);

        String newId = prefix + String.format("%06d", next);

        // Tạo account mới
        Account acc = new Account();
        acc.setAccountId(newId);
        acc.setUsername(request.getUsername());
        acc.setPassword(passwordEncoder.encode(request.getPassword()));
        acc.setFullName(request.getFullName());
        acc.setGender(request.getGender());
        acc.setEmail(request.getEmail());
        acc.setPhone(request.getPhone());

        // Gán role
        Role role = roleRepository.findById(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRoleName()));
        acc.setRole(role);

        // Lưu vào DB
        Account saved = accountRepository.save(acc);
        return mapToResponse(saved);
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

        if (account.getServiceCenter() != null)
            res.setServiceCenter(new ServiceCenterResponse(
                    account.getServiceCenter().getCenterId(),
                    account.getServiceCenter().getCenterName(),
                    account.getServiceCenter().getLocation()
            ));
        return res;
    }
}
