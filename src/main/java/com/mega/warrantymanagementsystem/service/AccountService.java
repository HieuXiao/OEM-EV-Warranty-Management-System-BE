package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.model.request.AccoutRequest;
import com.mega.warrantymanagementsystem.model.request.LoginRequest;
import com.mega.warrantymanagementsystem.model.request.UpdateRequest;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    AccountResponse  findByUsername(String username);
    AccountResponse  findByEmail(String email);
    AccountResponse  findByAccountId(String accountId);

    AccountResponse  createAccount(AccoutRequest accountRequest);
    AccountResponse  updateAccount(String id,UpdateRequest updateRequest);
    void deleteAccount(String accountId);

    AccountResponse login(LoginRequest loginRequest);
    AccountResponse  getCurrentAccount();

    List<AccountResponse > getAccounts();

    void addServiceCenterToAccount(String accountId, int centerId);
    void removeServiceCenterFromAccount(String accountId);

    List<AccountResponse > getAccountsByServiceCenter(int centerId);
}
