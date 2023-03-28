package de.telekom.customerapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    private CustomerRepository customerRepository;

    public CustomerController(CustomerRepository kundenRepository) {
        this.customerRepository = kundenRepository;
    }

    @GetMapping("")
    public List<Customer> index() {
        return customerRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Customer> get(@PathVariable int id) {
        try {
            Customer customer = customerService.getCustomer(id);
            return new ResponseEntity<Customer>(customer, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/id")
    public void add(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@RequestBody Customer customerDetails, @PathVariable int id) {
        try {
            Customer customer = customerService.getCustomer(id);
            customer.setVorname(customerDetails.getVorname());
            customer.setNachname(customerDetails.getNachname());
            Customer update = customerService.saveCustomer(customer);
            return  ResponseEntity.ok(update);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id)
    {
        customerService.deleteCustomer(id);
    }
}
