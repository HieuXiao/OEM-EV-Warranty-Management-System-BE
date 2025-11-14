package com.mega.warrantymanagementsystem.service.v2;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import com.mega.warrantymanagementsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashBoardService {

    @Autowired
    PartUnderWarrantyRepository partUnderWarrantyRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    CampaignRepository campaignRepository;

    public Map<String,Object> getDashboardStats(){
        Map<String,Object> stats = new HashMap<>();
        // Đếm số sản phẩm trong hệ thống
        long totalProducts = partUnderWarrantyRepository.count();
        stats.put("totalProducts",totalProducts);

        // Đếm số lượng khách hàng
        long totalCustomers = customerRepository.count();
        stats.put("totalCustomers",totalCustomers);

        // Đếm số lượng User
        long totalUsers = accountRepository.count();
        stats.put("totalUsers",totalUsers);

        // Đếm số lượng Vehicle
        long totalVehicles = vehicleRepository.count();
        stats.put("totalVehicles",totalVehicles);

        // Đếm số lượng Warranty Claim
        long totalWarrantyClaims = warrantyClaimRepository.count();
        stats.put("totalWarrantyClaims",totalWarrantyClaims);

        // Đếm số lượng chiến dịch
        long totalCampaigns = campaignRepository.count();
        stats.put("totalCampaigns",totalCampaigns);

        return stats;
    }

    // -------- WARRANTY CLAIM THEO THÁNG --------
    public Map<Integer, Long> getClaimsByMonth(int year) {
        Map<Integer, Long> result = new HashMap<>();

        List<Object[]> rows = warrantyClaimRepository.countClaimsByMonth(year);

        for (Object[] row : rows) {
            int month = ((Number) row[0]).intValue();
            long total = ((Number) row[1]).longValue();
            result.put(month, total);
        }

        return result;
    }


    // -------- WARRANTY CLAIM THEO NGÀY --------
    public Map<String, Long> getClaimsByDay(int year, int month) {
        Map<String, Long> result = new LinkedHashMap<>();

        List<Object[]> rows = warrantyClaimRepository.countClaimsByDay(year, month);

        for (Object[] row : rows) {
            String date = row[0].toString();
            long total = ((Number) row[1]).longValue();
            result.put(date, total);
        }

        return result;
    }


    // -------- ACCOUNT THEO ROLE --------
    public Map<String, Object> getAccountStats() {
        Map<String, Object> data = new HashMap<>();

        for (RoleName role : RoleName.values()) {
            List<Account> accounts = accountRepository.findByRole_RoleName(role);

            long enabled = accounts.stream().filter(Account::isEnabled).count();
            long disabled = accounts.size() - enabled;

            Map<String, Long> roleStats = new HashMap<>();
            roleStats.put("enabled", enabled);
            roleStats.put("disabled", disabled);
            roleStats.put("total", (long) accounts.size());

            data.put(role.name(), roleStats);
        }

        return data;
    }


}
