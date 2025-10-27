package com.mega.warrantymanagementsystem.controller;

import com.mega.warrantymanagementsystem.entity.Customer;
import com.mega.warrantymanagementsystem.model.request.CustomerRequest;
import com.mega.warrantymanagementsystem.model.response.CustomerResponse;
import com.mega.warrantymanagementsystem.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin//cho phép mọi nguồn truy cập
@SecurityRequirement(name = "api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<CustomerResponse> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public CustomerResponse getById(@PathVariable int id) {
        return customerService.getById(id)
                .map(customer -> customerService.getAll()
                        .stream()
                        .filter(c -> c.getCustomerId() == id)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Customer not found")))
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @PostMapping
    public CustomerResponse create(@RequestBody CustomerRequest request) {
        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setCustomerPhone(request.getCustomerPhone());
        customer.setCustomerEmail(request.getCustomerEmail());
        customer.setCustomerAddress(request.getCustomerAddress());
        return toResponse(customerService.create(customer));
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable int id, @RequestBody CustomerRequest request) {
        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setCustomerPhone(request.getCustomerPhone());
        customer.setCustomerEmail(request.getCustomerEmail());
        customer.setCustomerAddress(request.getCustomerAddress());
        return toResponse(customerService.update(id, customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        customerService.delete(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setCustomerId(customer.getCustomerId());
        response.setCustomerName(customer.getCustomerName());
        response.setCustomerPhone(customer.getCustomerPhone());
        response.setCustomerEmail(customer.getCustomerEmail());
        response.setCustomerAddress(customer.getCustomerAddress());
        // nếu muốn, có thể map vehicles sang VehicleResponse, hoặc bỏ nếu không cần
        return response;
    }

    @PostMapping("/{customerId}/assign-service-center/{serviceCenterId}")
    public ResponseEntity<String> assignServiceCenter(
            @PathVariable int customerId,
            @PathVariable int serviceCenterId) {

        String result = customerService.assignServiceCenter(customerId, serviceCenterId);

        if ("customer not found".equals(result) || "service center not found".equals(result)) {
            return ResponseEntity.status(404).body(result);
        } else if ("customer already assigned to this service center".equals(result)) {
            return ResponseEntity.status(400).body(result);
        }

        return ResponseEntity.ok(result);
    }
}
