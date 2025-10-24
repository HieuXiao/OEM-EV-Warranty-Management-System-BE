package com.mega.warrantymanagementsystem.service.impl;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.ServiceCenter;
import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.repository.AccountRepository;
import com.mega.warrantymanagementsystem.service.EvmStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class EvmStaffServiceImpl implements EvmStaffService {

    @Autowired
    private AccountRepository accountRepository;

    private final Set<String> usedIds = Collections.synchronizedSet(new HashSet<>());
    private int currentIndex = 0;

    /**
     * Lấy toàn bộ danh sách EVM Staff có enabled = true
     */
    @Override
    public List<AccountResponse> getAllEvmStaffs() {
        List<Account> evmStaffs = accountRepository.findByRole_RoleNameAndEnabledTrue(RoleName.EVM_STAFF);

        return evmStaffs.stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Random EVM Staff đang bật (enabled = true), không trùng trong vòng hiện tại.
     * Khi hết danh sách thì reset lại.
     */
    @Override
    public synchronized AccountResponse getRandomEvmStaff() {
        List<Account> evmStaffs = accountRepository.findByRole_RoleNameAndEnabledTrue(RoleName.EVM_STAFF);

        if (evmStaffs.isEmpty()) {
            throw new IllegalStateException("No active EVM Staff accounts available.");
        }

        // Reset nếu đã hết vòng
        if (usedIds.size() >= evmStaffs.size()) {
            usedIds.clear();
        }

        // Lọc những người chưa được random
        List<Account> remaining = evmStaffs.stream()
                .filter(acc -> !usedIds.contains(acc.getAccountId()))
                .toList();

        if (remaining.isEmpty()) {
            usedIds.clear();
            remaining = evmStaffs;
        }

        // Random 1 người
        int randomIndex = ThreadLocalRandom.current().nextInt(remaining.size());
        Account chosen = remaining.get(randomIndex);
        usedIds.add(chosen.getAccountId());

        return mapToResponse(chosen);
    }

    /**
     * Trả về EVM Staff kế tiếp (tuần tự, có vòng lặp)
     */
    @Override
    public synchronized AccountResponse getNextEvmStaffSequential() {
        List<Account> evmStaffs = accountRepository.findByRole_RoleNameAndEnabledTrue(RoleName.EVM_STAFF)
                .stream()
                .sorted(Comparator.comparing(Account::getAccountId))
                .toList();

        if (evmStaffs.isEmpty()) {
            throw new IllegalStateException("No active EVM Staff accounts available.");
        }

        if (currentIndex >= evmStaffs.size()) {
            currentIndex = 0;
        }

        Account chosen = evmStaffs.get(currentIndex);
        currentIndex++;

        return mapToResponse(chosen);
    }

    /**
     * Map Account entity sang DTO
     */
    private AccountResponse mapToResponse(Account account) {
        AccountResponse res = new AccountResponse();
        res.setAccountId(account.getAccountId());
        res.setUsername(account.getUsername());
        res.setFullName(account.getFullName());
        res.setGender(account.getGender());
        res.setEmail(account.getEmail());
        res.setPhone(account.getPhone());
        res.setEnabled(account.isEnabled());
        res.setRoleName(account.getRole().getRoleName().name());

        if (account.getServiceCenter() != null) {
            ServiceCenter sc = account.getServiceCenter();
            res.setServiceCenter(new ServiceCenterResponse(
                    sc.getCenterId(),
                    sc.getCenterName(),
                    sc.getLocation()
            ));
        }
        return res;
    }
}
