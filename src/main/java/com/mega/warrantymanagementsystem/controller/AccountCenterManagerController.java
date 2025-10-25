//package com.mega.warrantymanagementsystem.controller;
//
//import com.mega.warrantymanagementsystem.model.response.AccountResponse;
//import com.mega.warrantymanagementsystem.service.AccountService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/account-center")
//@CrossOrigin
//@SecurityRequirement(name = "api")
//public class AccountCenterManagerController {
//
//    @Autowired
//    private AccountService accountService;
//
//    @GetMapping("/search/center/{centerId}")
//    public ResponseEntity<List<AccountResponse>> getAccountsByCenter(@PathVariable int centerId) {
//        return ResponseEntity.ok(accountService.getAccountsByServiceCenter(centerId));
//    }
//
//    @PutMapping("/assign/{accountId}/{centerId}")
//    @Transactional
//    public ResponseEntity<String> assignCenterToAccount(
//            @PathVariable String accountId,
//            @PathVariable int centerId) {
//
//        accountService.addServiceCenterToAccount(accountId, centerId);
//        return ResponseEntity.ok("Assigned Service Center " + centerId + " to account " + accountId);
//    }
//
//    @PutMapping("/remove/{accountId}")
//    @Transactional
//    public ResponseEntity<String> removeCenterFromAccount(@PathVariable String accountId) {
//        accountService.removeServiceCenterFromAccount(accountId);
//        return ResponseEntity.ok("Removed Service Center from account " + accountId);
//    }
//}
