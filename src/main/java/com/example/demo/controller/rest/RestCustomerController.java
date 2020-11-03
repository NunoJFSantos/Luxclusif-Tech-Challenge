package com.example.demo.controller.rest;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/luxclusif/customer")
public class RestCustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> listCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer, UriComponentsBuilder uriComponentsBuilder) {

        Customer newCustomer = customerService.addCustomer(customer);

        UriComponents uriComponents = uriComponentsBuilder.path("api/luxclusif/customer/" + newCustomer.getId()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Customer> showCustomerById(@PathVariable("id") Integer id) {

        if (customerService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @Valid @RequestBody Customer customer, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (customer.getId() != null && !customer.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (customerService.findById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        customerService.updateCustomer(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public void deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomerById(id);
    }
}
