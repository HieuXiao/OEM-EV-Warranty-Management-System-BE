package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Account;
import com.mega.warrantymanagementsystem.entity.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByUsername(String username);//find by username
    Account findByEmail(String email);//find by email
    Account findByAccountId(String accountId);//find by accountId

    List<Account> findByServiceCenter_CenterId(int centerId);

    List<Account> findByRole_RoleName(RoleName roleName);
    List<Account> findByRole_RoleNameAndEnabledTrue(RoleName roleName);
    boolean existsByAccountId(String accountId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
