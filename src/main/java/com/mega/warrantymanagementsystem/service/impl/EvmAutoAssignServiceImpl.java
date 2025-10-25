//package com.mega.warrantymanagementsystem.service.impl;
//
//import com.mega.warrantymanagementsystem.entity.Account;
//import com.mega.warrantymanagementsystem.entity.WarrantyClaim;
//import com.mega.warrantymanagementsystem.entity.entity.WarrantyClaimStatus;
//import com.mega.warrantymanagementsystem.exception.exception.BusinessLogicException;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.response.AccountResponse;
//import com.mega.warrantymanagementsystem.model.response.WarrantyClaimResponse;
//import com.mega.warrantymanagementsystem.repository.AccountRepository;
//import com.mega.warrantymanagementsystem.repository.WarrantyClaimRepository;
//import com.mega.warrantymanagementsystem.service.EvmAutoAssignService;
//import com.mega.warrantymanagementsystem.service.EvmStaffService;
//import jakarta.transaction.Transactional;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EvmAutoAssignServiceImpl implements EvmAutoAssignService {
//
//    @Autowired
//    private WarrantyClaimRepository warrantyClaimRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private EvmStaffService evmStaffService;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    /**
//     * Gán EVM Staff tự động (theo thứ tự tuần tự)
//     */
//    @Override
//    @Transactional
//    public WarrantyClaimResponse autoAssignEvmToClaim(int claimId) {
//        WarrantyClaim claim = warrantyClaimRepository.findById(claimId)
//                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
//
//        boolean hasComponent = !claim.getClaimAttachments().isEmpty()
//                || !claim.getClaimReplacementParts().isEmpty()
//                || !claim.getServiceRecords().isEmpty();
//
//        if (!hasComponent) {
//            throw new BusinessLogicException("Cannot assign EVM before adding parts or records");
//        }
//
//        // Lấy EVM Staff kế tiếp (đã đảm bảo enabled=true)
//        AccountResponse nextEvm = evmStaffService.getNextEvmStaffSequential();
//
//        if (nextEvm == null || nextEvm.getAccountId() == null) {
//            throw new BusinessLogicException("No EVM Staff available for assignment");
//        }
//
//        // Lấy entity gốc từ DB
//        Account evmEntity = accountRepository.findByAccountId(nextEvm.getAccountId());
//        if (evmEntity == null) {
//            throw new ResourceNotFoundException("EVM account not found: " + nextEvm.getAccountId());
//        }
//
//        // Kiểm tra lại enabled trong DB (phòng trường hợp bị tắt giữa chừng)
//        if (!evmEntity.isEnabled()) {
//            throw new BusinessLogicException("Selected EVM Staff is not enabled: " + evmEntity.getAccountId());
//        }
//
//        // Gán và cập nhật trạng thái
//        claim.setEvm(evmEntity);
//        claim.setStatus(WarrantyClaimStatus.REPAIR);
//
//        WarrantyClaim saved = warrantyClaimRepository.save(claim);
//
//        WarrantyClaimResponse response = modelMapper.map(saved, WarrantyClaimResponse.class);
//        response.setEvmId(evmEntity.getAccountId());
//        return response;
//    }
//}
