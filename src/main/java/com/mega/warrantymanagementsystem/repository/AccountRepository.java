package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.Optional;

=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByUsername(String username);//find by username
    Account findByEmail(String email);//find by email
    Account findByAccountId(String accountId);//find by accountId
}
