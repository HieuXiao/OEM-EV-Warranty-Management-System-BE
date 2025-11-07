package com.mega.warrantymanagementsystem.service;


import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.Role;
import com.mega.warrantymanagementsystem.entity.ServiceCenter;
import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import com.mega.warrantymanagementsystem.model.EmailDetail;
import com.mega.warrantymanagementsystem.model.request.UpdatePasswordRequest;
import org.springframework.security.authentication.BadCredentialsException;
import com.mega.warrantymanagementsystem.exception.exception.ResourceNotFoundException;
import com.mega.warrantymanagementsystem.model.request.AccountRequest;
import com.mega.warrantymanagementsystem.model.request.LoginRequest;
import com.mega.warrantymanagementsystem.model.request.UpdateRequest;
import com.mega.warrantymanagementsystem.model.response.AccountResponse;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.repository.AccountRepository;
import com.mega.warrantymanagementsystem.repository.RoleRepository;
import com.mega.warrantymanagementsystem.repository.ServiceCenterRepository;
import com.mega.warrantymanagementsystem.service.TokenService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

//    @Autowired
//    EmailService emailService;

    @Autowired
    EmailService emailService;


    public AccountResponse findByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) throw new ResourceNotFoundException("Account not found: " + username);
        return mapToResponse(account);
    }


    public AccountResponse findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) throw new ResourceNotFoundException("Account not found with email: " + email);
        return mapToResponse(account);
    }


    public AccountResponse findByAccountId(String accountId) {
        Account account = accountRepository.findByAccountId(accountId.toUpperCase());
        if (account == null) throw new ResourceNotFoundException("Account not found with ID: " + accountId);
        return mapToResponse(account);
    }


    public AccountResponse createAccount(AccountRequest accoutRequest) {
        String accountId = accoutRequest.getAccountId().toUpperCase();

        // kiểm tra trùng
        if (accountRepository.existsByAccountId(accountId)) {
            throw new IllegalArgumentException("account id already exists");
        }
        if (accountRepository.existsByUsername(accoutRequest.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }
        if (accountRepository.existsByEmail(accoutRequest.getEmail())) {
            throw new IllegalArgumentException("email already exists");
        }
        if (accountRepository.existsByPhone(accoutRequest.getPhone())) {
            throw new IllegalArgumentException("phone already exists");
        }

        Account account = modelMapper.map(accoutRequest, Account.class);
        account.setAccountId(accountId);
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // xác định role theo prefix ID
        String prefix = account.getAccountId().substring(0, 2);
        RoleName roleName = switch (prefix) {
            case "AD" -> RoleName.ADMIN;
            case "SS" -> RoleName.SC_STAFF;
            case "ST" -> RoleName.SC_TECHNICIAN;
            case "ES" -> RoleName.EVM_STAFF;
            default -> throw new IllegalArgumentException("Invalid account prefix: " + prefix);
        };

        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found in DB: " + roleName));
        account.setRole(role);

        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }


    public AccountResponse updateAccount(String accountId, UpdateRequest updateRequest) {
        Account account = accountRepository.findById(accountId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        if (accountRepository.existsByUsername(updateRequest.getUsername())
                && !updateRequest.getUsername().equals(account.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }
        if (accountRepository.existsByEmail(updateRequest.getEmail())
                && !updateRequest.getEmail().equals(account.getEmail())) {
            throw new IllegalArgumentException("email already exists");
        }
        if (accountRepository.existsByPhone(updateRequest.getPhone())
                && !updateRequest.getPhone().equals(account.getPhone())) {
            throw new IllegalArgumentException("phone already exists");
        }

        account.setUsername(updateRequest.getUsername());
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        account.setFullName(updateRequest.getFullName());
        account.setGender(updateRequest.getGender());
        account.setEmail(updateRequest.getEmail());
        account.setPhone(updateRequest.getPhone());

        return mapToResponse(accountRepository.save(account));
    }



    public void deleteAccount(String accountId) {
        accountRepository.deleteById(accountId.toUpperCase());
    }


    public AccountResponse login(LoginRequest loginRequest) {
        String inputId = loginRequest.getAccountId().toUpperCase();
        Account account = accountRepository.findByAccountId(inputId);
        if (account == null || !passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
            throw new BadCredentialsException("Invalid accountId or password");
        }

        if (!account.isEnabled()) {
            throw new BadCredentialsException("This account has been disabled. Please contact the administrator.");
        }

        String token = tokenService.generateToken(account);
        AccountResponse response = mapToResponse(account);
        response.setToken(token);
        return response;
    }


    public AccountResponse getCurrentAccount() {
        Account current = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mapToResponse(current);
    }


    public List<AccountResponse> getAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username);
    }

    @Transactional

    public void addServiceCenterToAccount(String accountId, int centerId) {
        Account account = accountRepository.findById(accountId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        ServiceCenter serviceCenter = serviceCenterRepository.findById(centerId)
                .orElseThrow(() -> new ResourceNotFoundException("Service Center not found: " + centerId));

        account.setServiceCenter(serviceCenter);
        accountRepository.save(account);
    }


    @Transactional

    public void removeServiceCenterFromAccount(String accountId) {
        Account account = accountRepository.findById(accountId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        account.setServiceCenter(null);
        accountRepository.save(account);
    }


    public List<AccountResponse> getAccountsByServiceCenter(int centerId) {
        return accountRepository.findByServiceCenter_CenterId(centerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public AccountResponse updateAccountStatus(String accountId, boolean enabled) {
        Account account = accountRepository.findById(accountId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        account.setEnabled(enabled);
        return mapToResponse(accountRepository.save(account));
    }

    @Transactional
    public String updatePassword(String accountId, String oldPassword, String newPassword) {
        Account account = accountRepository.findById(accountId.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new BadCredentialsException("Old password is incorrect");
        }

        // Mã hóa và cập nhật mật khẩu mới
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        return "Password updated successfully for account " + accountId;
    }

    private AccountResponse mapToResponse(Account account) {
        AccountResponse res = new AccountResponse();
        res.setAccountId(account.getAccountId());
        res.setUsername(account.getUsername());
        res.setFullName(account.getFullName());
        res.setGender(account.getGender());
        res.setEmail(account.getEmail());
        res.setPhone(account.getPhone());

        // --- MỚI: set enabled
        res.setEnabled(account.isEnabled());

        if (account.getRole() != null) {
            res.setRoleName(account.getRole().getRoleName().name());
        }

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



    public String assignServiceCenterToAccount(String accountId, int serviceCenterId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId.toUpperCase());
        if (accountOpt.isEmpty()) {
            return "account not found";
        }
        Account account = accountOpt.get();

        String prefix = account.getAccountId().toUpperCase();
        if (!prefix.startsWith("SS") && !prefix.startsWith("ST")) {
            return "only SC Staff or SC Technician can be assigned a service center";
        }

        Optional<ServiceCenter> centerOpt = serviceCenterRepository.findById(serviceCenterId);
        if (centerOpt.isEmpty()) {
            return "service center not found";
        }
        ServiceCenter center = centerOpt.get();

        // check nếu account đã có service center
        if (account.getServiceCenter() != null) {
            if (account.getServiceCenter().getCenterId() == serviceCenterId) {
                return "account already assigned to this service center";
            } else {
                return "account already assigned to another service center";
            }
        }

        account.setServiceCenter(center);
        accountRepository.save(account);

        return "service center assigned to account successfully";
    }
    //cơ chế
    //B1: lấy username người dùng nhập
    //B2: tìm trong DB xem có account nào trùng với username đó không (LoadUserByUsername)
    //B3: authenticationManager => compare tk password dưới db <=> password người dùng nhập


    @Transactional
    public String changeServiceCenterForAccount(String accountId, int newServiceCenterId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId.toUpperCase());
        if (accountOpt.isEmpty()) {
            return "account not found";
        }
        Account account = accountOpt.get();

        // check if account currently has a service center
        if (account.getServiceCenter() == null) {
            return "cannot change because this account has no service center assigned";
        }

        // get new service center
        Optional<ServiceCenter> centerOpt = serviceCenterRepository.findById(newServiceCenterId);
        if (centerOpt.isEmpty()) {
            return "new service center not found";
        }
        ServiceCenter newCenter = centerOpt.get();

        // check if it's already the same
        if (account.getServiceCenter().getCenterId() == newServiceCenterId) {
            return "this account is already assigned to the selected service center";
        }

        // update and save
        account.setServiceCenter(newCenter);
        accountRepository.save(account);

        return "service center changed successfully for account " + accountId;
    }

    //forgot password
//    public void resetPassword(String email) {
//
//        Account account = accountRepository.findByEmail(email);
//        String token = tokenService.generateToken(account);
//        String url = "http://localhost:5173/reset-password?token="+token;
//
//        EmailDetail emailDetail = new EmailDetail();
//        emailDetail.setSubject("Rest password");
//        emailDetail.setRecipient(account.getEmail());
//        emailDetail.setFullName(account.getFullName());
//        emailDetail.setUrl(url);
//
//        emailService.sendMailTemplate(emailDetail,"forgot-password.html");
//    }

    public void resetPassword(String email) {

        Account account = accountRepository.findByEmail(email);
        String token = tokenService.generateToken(account);
        String url = "http://localhost:5173/reset-password?token="+token;

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setSubject("Rest password");
        emailDetail.setRecipient(account.getEmail());
        emailDetail.setFullName(account.getFullName());
        emailDetail.setUrl(url);

        emailService.sendMailTemplate(emailDetail,"forgot-password.html");
    }

    @Transactional
    public AccountResponse updateForgotPassword(UpdatePasswordRequest updatePasswordRequest) {
        // Lấy người dùng hiện tại từ SecurityContext (token trong Authorization)
        Account currentAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Tìm lại bản ghi thật trong DB
        Account account = accountRepository.findById(currentAccount.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // Cập nhật mật khẩu mới
        account.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
        accountRepository.save(account);

        return mapToResponse(account);
    }


}
