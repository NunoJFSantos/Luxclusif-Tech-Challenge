package com.example.demo.controller.web;

import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void testShowSignUpForm() throws Exception {

        mvc.perform(get("/luxclusif/customer/signup"))
                .andExpect(status().isOk())
                .andExpect(model().size(2))
                .andExpect(model().attribute("title", "Sign Up"))
                .andExpect(model().attribute("customer", instanceOf(Customer.class)))
                .andExpect(view().name("customer/add"));

        verifyZeroInteractions(customerService);
    }


    @Test
    void testShowCustomerDetails() throws Exception {

        Integer fakeId = 999;

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        Product product2 = new Product();
        products.add(product1);
        products.add(product2);

        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName("Nuno");
        customer.setLastName("Santos");
        customer.setCity("Porto");
        customer.setCountry("Portugal");
        customer.setEmail("nuno@gmail.com");
        customer.setPhone("912345678");
        customer.setAddress("Rua de Cima, 123");
        customer.addProduct(product1);
        customer.addProduct(product2);

        when(customerService.findById(999)).thenReturn(customer);

        mvc.perform(MockMvcRequestBuilders.get("/luxclusif/customer/" + fakeId))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/details"))
                .andExpect(model().attribute("customer", equalTo(customer)))
                .andExpect(model().attribute("products", products))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().size(2));

        verify(customerService, times(1)).findById(fakeId);
    }

    @Test
    void testEditCustomer() throws Exception {

        Integer fakeId = 99;

        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName("Nuno");
        customer.setLastName("Santos");
        customer.setCity("Porto");
        customer.setCountry("Portugal");
        customer.setEmail("nuno@gmail.com");
        customer.setPhone("912345678");
        customer.setAddress("Rua de Cima, 123");

        when(customerService.findById(fakeId)).thenReturn(customer);

        mvc.perform(MockMvcRequestBuilders.get("/luxclusif/customer/" + fakeId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/edit"))
                .andExpect(model().attribute("customer", equalTo(customer)))
                .andExpect(model().size(1));

        verify(customerService, times(1)).findById(fakeId);
    }

    @Test
    void testAddCustomer() throws Exception {

        Integer fakeId = 99;
        String firstName = "Nuno";
        String lastName = "Santos";
        String email = "nuno@gmail.com";
        String phone = "912345678";
        String address = "Rua de Cima, 123";
        String city = "Porto";
        String country = "Portugal";

        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCity(city);
        customer.setCountry(country);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);

        when(customerService.addCustomer(any(Customer.class))).thenReturn(customer);

        mvc.perform(post("/luxclusif/customer/add")
                .param("id", fakeId.toString())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("phone", phone)
                .param("address", address)
                .param("city", city)
                .param("country", country))
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerService, times(1)).addCustomer(customerArgumentCaptor.capture());
    }

    @Test
    void testUpdateCustomer() throws Exception {

        Integer fakeId = 99;
        String firstName = "Nuno";
        String lastName = "Santos";
        String email = "nuno@gmail.com";
        String phone = "912345678";
        String address = "Rua de Cima, 123";
        String city = "Porto";
        String country = "Portugal";

        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setCity(city);
        customer.setCountry(country);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);

        when(customerService.updateCustomer(any(Customer.class))).thenReturn(customer);

        mvc.perform(post("/luxclusif/customer/edit")
        .param("id", fakeId.toString())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("phone", phone)
                .param("address", address)
                .param("city", city)
                .param("country", country))
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerService, times(1)).updateCustomer(customerArgumentCaptor.capture());
    }

    @Test
    void testDeleteCustomer() throws Exception {

        Integer fakeId = 99;

        Customer customer = new Customer();

        when(customerService.findById(fakeId)).thenReturn(customer);

        mvc.perform(post("/luxclusif/customer/" + fakeId + "/delete"))
                .andExpect(status().is3xxRedirection());

        verify(customerService, times(1)).deleteCustomerById(fakeId);
    }

}