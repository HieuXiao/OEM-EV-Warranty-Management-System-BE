package com.mega.warrantymanagementsystem.service.v2;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.Role;
import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.FullAutoAccountRequest;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.repository.AccountRepository;
import com.mega.warrantymanagementsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Tự động tạo accountId & username từ fullName và roleName.
 * accountId dùng cơ chế hole-filling.
 * username dựa trên họ tên, có hậu tố số tăng dần nếu trùng.
 */
@Service
public class FullAutoAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Đăng ký tài khoản hoàn toàn tự động.
     */
    @Transactional
    public AccountResponse registerFullAuto(FullAutoAccountRequest request) {

        // Sinh ID theo roleName (hole filling)
        String prefix = switch (request.getRoleName()) {
            case ADMIN -> "AD";
            case SC_STAFF -> "SS";
            case SC_TECHNICIAN -> "ST";
            case EVM_STAFF -> "ES";
        };
        String newAccountId = generateAccountId(prefix);

        // Sinh username độc nhất dựa theo full name
        String baseUsername = generateBaseUsername(request.getFullName());
        String uniqueUsername = generateUniqueUsername(baseUsername);

        // Kiểm tra email trùng
        if (request.getEmail() != null && accountRepository.findByEmail(request.getEmail()) != null) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // Lấy role từ DB
        Role role = roleRepository.findById(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRoleName()));

        // Tạo account mới
        Account acc = new Account();
        acc.setAccountId(newAccountId);
        acc.setUsername(uniqueUsername);
        acc.setFullName(request.getFullName());
        acc.setGender(request.getGender());
        acc.setEmail(request.getEmail());
        acc.setPhone(request.getPhone());
        acc.setPassword(passwordEncoder.encode(request.getPassword()));
        acc.setRole(role);
        acc.setEnabled(true);

        Account saved;
        try {
            saved = accountRepository.save(acc);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Conflict when saving new account (duplicate ID or username).");
        }

        return mapToResponse(saved);
    }

    /**
     * Sinh accountId theo cơ chế hole-filling (điền chỗ trống nhỏ nhất).
     */
    private String generateAccountId(String prefix) {
        List<String> ids = accountRepository.findAll().stream()
                .map(Account::getAccountId)
                .filter(Objects::nonNull)
                .filter(id -> id.startsWith(prefix))
                .collect(Collectors.toList());

        BitSet used = new BitSet(1_000_000);
        for (String id : ids) {
            if (id.length() == 8 && id.substring(2).matches("\\d{6}")) {
                used.set(Integer.parseInt(id.substring(2)));
            }
        }

        int next = used.nextClearBit(0);
        if (next > 999_999)
            throw new IllegalStateException("ID range exhausted for prefix " + prefix);

        return prefix + String.format("%06d", next);
    }

    /**
     * Tạo phần cơ bản cho username từ full name.
     * Ví dụ: "Trần Tiến Danh" → "DanhTT"
     */
    private String generateBaseUsername(String fullName) {
        if (fullName == null || fullName.isBlank()) return "User";

        // Bỏ dấu tiếng Việt
        String clean = removeVietnameseAccents(fullName).trim();
        String[] parts = clean.split("\\s+");
        if (parts.length == 1) return capitalize(parts[0]);

        String lastName = capitalize(parts[parts.length - 1]);
        String initials = Arrays.stream(parts, 0, parts.length - 1)
                .map(s -> s.substring(0, 1).toUpperCase())
                .collect(Collectors.joining());
        return lastName + initials;
    }

    /**
     * Sinh username duy nhất (base + 01, 02, 03…)
     */
    private String generateUniqueUsername(String base) {
        List<String> existing = accountRepository.findAll().stream()
                .map(Account::getUsername)
                .filter(Objects::nonNull)
                .filter(u -> u.toLowerCase().startsWith(base.toLowerCase()))
                .collect(Collectors.toList());

        // Nếu chưa có username nào bắt đầu bằng base
        if (existing.isEmpty()) return base + "01";

        // Thu thập tất cả hậu tố số (01, 02...)
        Set<Integer> usedNumbers = new HashSet<>();
        Pattern pattern = Pattern.compile(Pattern.quote(base) + "(\\d+)$", Pattern.CASE_INSENSITIVE);

        for (String u : existing) {
            Matcher m = pattern.matcher(u);
            if (m.find()) {
                try {
                    usedNumbers.add(Integer.parseInt(m.group(1)));
                } catch (NumberFormatException ignored) {}
            }
        }

        // Nếu không có hậu tố nào, thêm 01
        if (usedNumbers.isEmpty()) {
            if (!existing.contains(base + "01")) return base + "01";
        }

        // Tìm số nhỏ nhất chưa dùng
        int next = 1;
        while (usedNumbers.contains(next)) {
            next++;
        }

        return base + String.format("%02d", next);
    }

    private String removeVietnameseAccents(String input) {
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        return temp.replaceAll("\\p{M}", "");
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
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
