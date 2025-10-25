//package com.mega.warrantymanagementsystem.service.impl;
//
//
//import com.mega.warrantymanagementsystem.entity.Account;
//import com.mega.warrantymanagementsystem.entity.Role;
//import com.mega.warrantymanagementsystem.entity.ServiceCenter;
//import com.mega.warrantymanagementsystem.entity.entity.RoleName;
//import org.springframework.security.authentication.BadCredentialsException;
//import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
//import com.mega.warrantymanagementsystem.model.request.AccoutRequest;
//import com.mega.warrantymanagementsystem.model.request.LoginRequest;
//import com.mega.warrantymanagementsystem.model.request.UpdateRequest;
//import com.mega.warrantymanagementsystem.model.response.AccountResponse;
//import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
//import com.mega.warrantymanagementsystem.repository.AccountRepository;
//import com.mega.warrantymanagementsystem.repository.RoleRepository;
//import com.mega.warrantymanagementsystem.repository.ServiceCenterRepository;
//import com.mega.warrantymanagementsystem.service.AccountService;
//import com.mega.warrantymanagementsystem.service.TokenService;
//import jakarta.transaction.Transactional;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class AccountServiceImpl implements AccountService, UserDetailsService {
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    RoleRepository roleRepository;
//
//    @Autowired
//    private ServiceCenterRepository serviceCenterRepository;
//
//    @Autowired
//    ModelMapper modelMapper;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Autowired
//    TokenService tokenService;
//
//    @Override
//    public AccountResponse findByUsername(String username) {
//        Account account = accountRepository.findByUsername(username);
//        if (account == null) throw new ResourceNotFoundException("Account not found: " + username);
//        return mapToResponse(account);
//    }
//
//    @Override
//    public AccountResponse findByEmail(String email) {
//        Account account = accountRepository.findByEmail(email);
//        if (account == null) throw new ResourceNotFoundException("Account not found with email: " + email);
//        return mapToResponse(account);
//    }
//
//    @Override
//    public AccountResponse findByAccountId(String accountId) {
//        Account account = accountRepository.findByAccountId(accountId.toUpperCase());
//        if (account == null) throw new ResourceNotFoundException("Account not found with ID: " + accountId);
//        return mapToResponse(account);
//    }
//
//    @Override
//    public AccountResponse createAccount(AccoutRequest accoutRequest) {
//        Account account = modelMapper.map(accoutRequest, Account.class);
//        account.setAccountId(account.getAccountId().toUpperCase());
//        account.setPassword(passwordEncoder.encode(account.getPassword()));
//
//        // xác định role theo prefix ID
//        String prefix = account.getAccountId().substring(0, 2);
//        RoleName roleName = switch (prefix) {
//            case "AD" -> RoleName.ADMIN;
//            case "SS" -> RoleName.SC_STAFF;
//            case "ST" -> RoleName.SC_TECHNICIAN;
//            case "ES" -> RoleName.EVM_STAFF;
//            default -> throw new IllegalArgumentException("Invalid account prefix: " + prefix);
//        };
//
//        Role role = roleRepository.findById(roleName)
//                .orElseThrow(() -> new IllegalArgumentException("Role not found in DB: " + roleName));
//        account.setRole(role);
//
//        Account saved = accountRepository.save(account);
//        return mapToResponse(saved);
//    }
//
//    @Override
//    public AccountResponse updateAccount(String accountId, UpdateRequest updateRequest) {
//        Account account = accountRepository.findById(accountId.toUpperCase())
//                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
//
//        account.setUsername(updateRequest.getUsername());
//        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
//            account.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
//        }
//        account.setFullName(updateRequest.getFullName());
//        account.setGender(updateRequest.getGender());
//        account.setEmail(updateRequest.getEmail());
//        account.setPhone(updateRequest.getPhone());
//
//        return mapToResponse(accountRepository.save(account));
//    }
//
//
//    @Override
//    public void deleteAccount(String accountId) {
//        accountRepository.deleteById(accountId.toUpperCase());
//    }
//
//    @Override
//    public AccountResponse login(LoginRequest loginRequest) {
//        String inputId = loginRequest.getAccountId().toUpperCase();
//        Account account = accountRepository.findByAccountId(inputId);
//        if (account == null || !passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
//            throw new BadCredentialsException("Invalid accountId or password");
//        }
//
//        if (!account.isEnabled()) {
//            throw new BadCredentialsException("This account has been disabled. Please contact the administrator.");
//        }
//
//        String token = tokenService.generateToken(account);
//        AccountResponse response = mapToResponse(account);
//        response.setToken(token);
//        return response;
//    }
//
//    @Override
//    public AccountResponse getCurrentAccount() {
//        Account current = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return mapToResponse(current);
//    }
//
//    @Override
//    public List<AccountResponse> getAccounts() {
//        return accountRepository.findAll().stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return accountRepository.findByUsername(username);
//    }
//
//    @Transactional
//    @Override
//    public void addServiceCenterToAccount(String accountId, int centerId) {
//        Account account = accountRepository.findById(accountId.toUpperCase())
//                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
//
//        ServiceCenter serviceCenter = serviceCenterRepository.findById(centerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Service Center not found: " + centerId));
//
//        account.setServiceCenter(serviceCenter);
//        accountRepository.save(account);
//    }
//
//
//    @Transactional
//    @Override
//    public void removeServiceCenterFromAccount(String accountId) {
//        Account account = accountRepository.findById(accountId.toUpperCase())
//                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
//
//        account.setServiceCenter(null);
//        accountRepository.save(account);
//    }
//
//    @Override
//    public List<AccountResponse> getAccountsByServiceCenter(int centerId) {
//        return accountRepository.findByServiceCenter_CenterId(centerId).stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public AccountResponse updateAccountStatus(String accountId, boolean enabled) {
//        Account account = accountRepository.findById(accountId.toUpperCase())
//                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
//
//        account.setEnabled(enabled);
//        return mapToResponse(accountRepository.save(account));
//    }
//
//    private AccountResponse mapToResponse(Account account) {
//        AccountResponse res = new AccountResponse();
//        res.setAccountId(account.getAccountId());
//        res.setUsername(account.getUsername());
//        res.setFullName(account.getFullName());
//        res.setGender(account.getGender());
//        res.setEmail(account.getEmail());
//        res.setPhone(account.getPhone());
//
//        // --- MỚI: set enabled
//        res.setEnabled(account.isEnabled());
//
//        if (account.getRole() != null) {
//            res.setRoleName(account.getRole().getRoleName().name());
//        }
//
//        if (account.getServiceCenter() != null) {
//            ServiceCenter sc = account.getServiceCenter();
//            res.setServiceCenter(new ServiceCenterResponse(
//                    sc.getCenterId(),
//                    sc.getCenterName(),
//                    sc.getLocation()
//            ));
//        }
//
//        return res;
//    }
//
//
//
//
//    //cơ chế
//    //B1: lấy username người dùng nhập
//    //B2: tìm trong DB xem có account nào trùng với username đó không (LoadUserByUsername)
//    //B3: authenticationManager => compare tk password dưới db <=> password người dùng nhập
//}
