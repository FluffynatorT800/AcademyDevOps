package de.telekom.customerapi;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    public List<Customer> listAllCustomer() {
        return customerRepository.findAll();
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomer(int id) {
        return customerRepository.findById(id).get();
    }

    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }
}
