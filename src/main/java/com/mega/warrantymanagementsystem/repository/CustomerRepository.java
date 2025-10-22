<<<<<<< HEAD
<<<<<<< HEAD
    package com.mega.warrantymanagementsystem.repository;

    import com.mega.warrantymanagementsystem.entity.Customer;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface CustomerRepository extends JpaRepository<Customer, Integer> {
        Customer findByCustomerPhone(String phone);
    }
=======
=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
package com.mega.warrantymanagementsystem.repository;

import com.mega.warrantymanagementsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByCustomerPhone(String phone);
}
<<<<<<< HEAD
>>>>>>> origin/main
=======
>>>>>>> dd2688e4548ab8a0460d7d748184888d4f160c8c
