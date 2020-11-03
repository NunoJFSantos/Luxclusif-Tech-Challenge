package com.example.demo.service;

import com.example.demo.dao.CustomerDao;
import com.example.demo.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CustomerService.class})
public class CustomerServiceTest {

    @MockBean
    private CustomerDao customerDao;

    @Test
    void testGetAllCustomers() {

        List<Customer> fakeList = mock(ArrayList.class);

        when(customerDao.findAll()).thenReturn(fakeList);

        List<Customer> list = customerDao.findAll();

        assertNotNull(list);
        verify(customerDao, times(1)).findAll();
    }

    @Test
    void testFindById() {

        Integer fakeId = 99;

        Optional<Customer> fakeCustomer = Optional.of(new Customer());

        when(customerDao.findById(99)).thenReturn(fakeCustomer);

        customerDao.findById(fakeId);

        verify(customerDao, times(1)).findById(fakeId);
    }

    @Test
    void testAddCustomer() {

        Customer fakeCustomer = mock(Customer.class);

        when(customerDao.save(fakeCustomer)).thenReturn(fakeCustomer);

        Customer customer = customerDao.save(fakeCustomer);

        assertNotNull(customer);
        verify(customerDao, times(1)).save(fakeCustomer);
    }

    @Test
    void testUpdateCustomer() {

        Customer fakeCustomer = mock(Customer.class);

        when(customerDao.save(fakeCustomer)).thenReturn(fakeCustomer);

        Customer customer = customerDao.save(fakeCustomer);

        assertTrue(customer.equals(fakeCustomer));
        verify(customerDao, times(1)).save(fakeCustomer);
    }

    @Test
    void testDeleteCustomerById() {

        Integer fakeId = 99;

        customerDao.deleteById(fakeId);

        verify(customerDao, times(1)).deleteById(fakeId);

    }
}
