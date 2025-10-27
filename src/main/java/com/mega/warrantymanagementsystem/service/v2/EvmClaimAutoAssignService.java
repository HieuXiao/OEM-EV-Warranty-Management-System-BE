package com.mega.warrantymanagementsystem.service.v2;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.EvmAssignmentState;
import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
import com.mega.warrantymanagementsystem.repository.AccountRepository;
import com.mega.warrantymanagementsystem.repository.EvmAssignmentStateRepository;
import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EvmClaimAutoAssignService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WarrantyClaimRepository warrantyClaimRepository;

    @Autowired
    private EvmAssignmentStateRepository stateRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Tự động gán tất cả các claim có status = DECIDE cho các EVM staff (theo thứ tự vòng lặp, có ghi nhớ vị trí).
     */
    @Transactional
    public List<WarrantyClaimResponse> autoAssignDecideClaimsToEvm() {
        // 1️⃣ Lấy danh sách EVM staff đang bật
        List<Account> evmStaffs = accountRepository.findByRole_RoleNameAndEnabledTrue(RoleName.EVM_STAFF)
                .stream()
                .sorted(Comparator.comparing(Account::getAccountId))
                .collect(Collectors.toList());

        if (evmStaffs.isEmpty()) {
            throw new ResourceNotFoundException("Không có EVM Staff nào đang hoạt động.");
        }

        // 2️⃣ Lấy các claim có status = DECIDE và chưa gán EVM
        List<WarrantyClaim> pendingClaims = warrantyClaimRepository.findAll().stream()
                .filter(c -> c.getStatus() == WarrantyClaimStatus.DECIDE && c.getEvm() == null)
                .sorted(Comparator.comparing(WarrantyClaim::getClaimId))
                .collect(Collectors.toList());

        if (pendingClaims.isEmpty()) {
            throw new ResourceNotFoundException("Không có WarrantyClaim nào ở trạng thái DECIDE để gán.");
        }

        // 3️⃣ Lấy hoặc khởi tạo trạng thái chỉ số gán EVM
        EvmAssignmentState state = stateRepository.findById("EVM_ASSIGNMENT_TRACKER")
                .orElseGet(() -> {
                    EvmAssignmentState newState = new EvmAssignmentState();
                    newState.setLastIndex(-1);
                    return newState;
                });

        int index = state.getLastIndex();
        List<WarrantyClaimResponse> assignedClaims = new ArrayList<>();

        // 4️⃣ Gán từng claim cho EVM tiếp theo
        for (WarrantyClaim claim : pendingClaims) {
            index = (index + 1) % evmStaffs.size(); // Xoay vòng

            Account selectedEvm = evmStaffs.get(index);
            claim.setEvm(selectedEvm);
//            claim.setStatus(WarrantyClaimStatus.REPAIR);

            warrantyClaimRepository.save(claim);

            WarrantyClaimResponse response = modelMapper.map(claim, WarrantyClaimResponse.class);
            response.setStatus(WarrantyClaimStatus.REPAIR.name());
            assignedClaims.add(response);
        }

        // 5️⃣ Lưu lại chỉ số cuối cùng
        state.setLastIndex(index);
        stateRepository.save(state);

        return assignedClaims;
    }
}
