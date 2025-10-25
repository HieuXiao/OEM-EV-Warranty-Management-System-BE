//package com.mega.warrantymanagementsystem.controller;
//
//import com.mega.warrantymanagementsystem.model.response.AccountResponse;
//import com.mega.warrantymanagementsystem.service.EvmStaffService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/evm-staffs")
//@CrossOrigin
//@SecurityRequirement(name = "api")
//public class EvmStaffController {
//
//    @Autowired
//    private EvmStaffService evmStaffService;
//
//    /**
//     * API lấy toàn bộ danh sách nhân viên EVM
//     * @return danh sách tài khoản có role là EVM_STAFF
//     */
//    @GetMapping("/")
//    public ResponseEntity<List<AccountResponse>> getAllEvmStaffs() {
//        List<AccountResponse> evmStaffList = evmStaffService.getAllEvmStaffs();
//        return ResponseEntity.ok(evmStaffList);
//    }
//    @GetMapping("/sequential")
//    public ResponseEntity<AccountResponse> getNextEvmStaffSequential() {
//        AccountResponse nextStaff = evmStaffService.getNextEvmStaffSequential();
//        return ResponseEntity.ok(nextStaff);
//    }
//
//}
