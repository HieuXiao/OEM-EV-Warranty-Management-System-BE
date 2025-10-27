package com.mega.warrantymanagementsystem.service;

import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.entity.ServiceCenter;
import com.mega.warrantymanagementsystem.exception.exception.DuplicateResourceException;
import com.mega.warrantymanagementsystem.model.response.CustomerResponse;
import com.mega.warrantymanagementsystem.model.response.ServiceCenterResponse;
import com.mega.warrantymanagementsystem.repository.CustomerRepository;
import com.mega.warrantymanagementsystem.repository.ServiceCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    public List<CustomerResponse> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<Customer> getById(int id) {
        return customerRepository.findById(id);
    }

    public Customer create(Customer customer) {
        if (customerRepository.existsByCustomerEmail(customer.getCustomerEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (customerRepository.existsByCustomerPhone(customer.getCustomerPhone())) {
            throw new DuplicateResourceException("Phone already exists");
        }
        return customerRepository.save(customer);
    }

    public Customer update(int id, Customer customer) {
        return customerRepository.findById(id).map(existing -> {
            // check email trùng với customer khác
            if (customerRepository.existsByCustomerEmailAndCustomerIdNot(customer.getCustomerEmail(), id)) {
                throw new DuplicateResourceException("Email already exists");
            }
            // check phone trùng với customer khác
            if (customerRepository.existsByCustomerPhoneAndCustomerIdNot(customer.getCustomerPhone(), id)) {
                throw new DuplicateResourceException("Phone already exists");
            }
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

    public String assignServiceCenter(int customerId, int serviceCenterId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return "customer not found";
        }

        Customer customer = customerOpt.get(); // lấy ra object Customer

        if (customer.getServiceCenter() != null && customer.getServiceCenter().getCenterId() == serviceCenterId) {
            return "customer already assigned to this service center";
        }


        Optional<ServiceCenter> scOpt = serviceCenterRepository.findById(serviceCenterId);
        if (scOpt.isEmpty()) {
            return "service center not found";
        }

        customer.setServiceCenter(scOpt.get());
        customerRepository.save(customer);

        return "assign success";
    }

    // Map Entity sang Response
    private CustomerResponse mapToResponse(Customer customer) {
        ServiceCenterResponse scRes = null;
        if (customer.getServiceCenter() != null) {
            ServiceCenter sc = customer.getServiceCenter();
            scRes = new ServiceCenterResponse(
                    sc.getCenterId(),
                    sc.getCenterName(),
                    sc.getLocation()
            );
        }

        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getCustomerPhone(),
                customer.getCustomerEmail(),
                customer.getCustomerAddress(),
                scRes
        );
    }

}
