package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getById(int id) {
        return customerRepository.findById(id);
    }

    public Customer create(Customer customer) {
        if (customerRepository.existsByCustomerEmail(customer.getCustomerEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (customerRepository.existsByCustomerPhone(customer.getCustomerPhone())) {
            throw new RuntimeException("Phone already exists");
        }
        return customerRepository.save(customer);
    }

    public Customer update(int id, Customer customer) {
        return customerRepository.findById(id).map(existing -> {
            existing.setCustomerName(customer.getCustomerName());
            existing.setCustomerPhone(customer.getCustomerPhone());
            existing.setCustomerEmail(customer.getCustomerEmail());
            existing.setCustomerAddress(customer.getCustomerAddress());
            return customerRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public void delete(int id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.deleteById(id);
    }
}
