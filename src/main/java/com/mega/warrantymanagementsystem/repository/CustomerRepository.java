    package com.mega.warrantymanagementsystem.repository;

    import com.mega.warrantymanagementsystem.entity.Customer;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface CustomerRepository extends JpaRepository<Customer, Integer> {
        Customer findByCustomerPhone(String phone);
        Customer findByCustomerEmail(String email);
        List<Customer> findByCustomerNameContaining(String name);
        boolean existsByCustomerPhone(String phone);
        boolean existsByCustomerEmail(String email);
        boolean existsByCustomerEmailAndCustomerIdNot(String email, int customerId);
        boolean existsByCustomerPhoneAndCustomerIdNot(String phone, int customerId);

    }
