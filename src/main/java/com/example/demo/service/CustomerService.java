package com.example.demo.service;

import com.example.demo.dao.CustomerDao;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    public Customer findById(Integer id) {
        return customerDao.findById(id).orElse(null);
    }

    public Customer addCustomer(Customer customer) {

        Customer newCustomer = customerDao.save(customer);
        newCustomer.setLoggedIn(true);

        return newCustomer;
    }

    public Customer updateCustomer(Customer customer) {

        Customer updatedCustomer = customerDao.findById(customer.getId()).orElse(null);

        for (Product p : updatedCustomer.getProducts()) {
            customer.addProduct(p);
        }

        return customerDao.save(customer);
    }

    public void deleteCustomerById(Integer id) {
        customerDao.deleteById(id);
    }


}
