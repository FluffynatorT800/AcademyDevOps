package de.telekom.customerapi;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private CustomerRepository customerRepository;

    public CustomerController(CustomerRepository kundenRepository) {
        this.customerRepository = kundenRepository;
    }

    @GetMapping("")
    public List<Customer> index() {
        return customerRepository.findAll();
    }

    @PostMapping("/plus")
    public Customer createcustomer(
        @RequestBody Customer customer
    )
    {
        return customerRepository.save(customer);
    }

    @GetMapping("/customer/{id}")
    public Optional<Customer> getNoteByID(@PathVariable(value = "id") int id)
    {
        return customerRepository.findById(id);
    }
}
