package com.example.demo.controller.web;

import com.example.demo.model.Customer;
import com.example.demo.model.ImageFile;
import com.example.demo.model.Product;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private final String REQ_MAP = "/luxclusif/customer/";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CustomerService customerService;

    @Test
    void testListProducts() throws Exception {

        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(99);

        Product fakeProduct1 = new Product();
        Product fakeProduct2 = new Product();

        fakeProduct1.setCustomer(fakeCustomer);
        fakeProduct2.setCustomer(fakeCustomer);

        List<Product> products = new ArrayList<>();
        products.add(fakeProduct1);
        products.add(fakeProduct2);

        when(productService.listAllProducts()).thenReturn(products);

        mvc.perform(get(REQ_MAP + "product"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", products))
                .andExpect(model().attribute("products", hasSize(2)))
                .andExpect(model().size(1))
                .andExpect(view().name("product/list"));

        verify(productService, times(1)).listAllProducts();
    }

    @Test
    void testShowProductForm() throws Exception {

        Integer fakeId = 99;

        Customer fakeCustomer = new Customer();

        when(customerService.findById(fakeId)).thenReturn(fakeCustomer);

        mvc.perform(get(REQ_MAP + fakeId + "/product/add"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", fakeCustomer))
                .andExpect(model().attribute("product", instanceOf(Product.class)))
                .andExpect(model().size(2))
                .andExpect(view().name("product/add"));

        verify(customerService, times(2)).findById(fakeId);
        verifyZeroInteractions(productService);
    }

    @Test
    void testShowEditProduct() throws Exception {

        Integer fakeCustomerId = 99;
        Integer fakeProductId = 88;

        Product fakeProduct = new Product();
        fakeProduct.setId(fakeProductId);
        fakeProduct.setBrand("Brand");
        fakeProduct.setYearOfPurchase("1999");
        fakeProduct.setProductCondition("Good");
        fakeProduct.setPrice(999.99);

        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        fakeCustomer.setFirstName("Nuno");
        fakeCustomer.setLastName("Santos");
        fakeCustomer.setCity("Porto");
        fakeCustomer.setCountry("Portugal");
        fakeCustomer.setEmail("nuno@gmail.com");
        fakeCustomer.setPhone("912345678");
        fakeCustomer.setAddress("Rua de Cima, 123");
        fakeCustomer.addProduct(fakeProduct);

        when(customerService.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(productService.getProductById(fakeProductId)).thenReturn(fakeProduct);

        mvc.perform(get(REQ_MAP + fakeCustomerId + "/product/" + fakeProductId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", fakeCustomer))
                .andExpect(model().attribute("product", fakeProduct))
                .andExpect(model().size(2))
                .andExpect(view().name("product/edit"));

        verify(customerService, times(1)).findById(fakeCustomerId);
        verify(productService, times(1)).getProductById(fakeProductId);
    }

    @Test
    void testShowProductImages() throws Exception {

        Integer fakeProductId = 99;
        Integer fakeCustomerId = 88;

        ImageFile image1 = mock(ImageFile.class);
        ImageFile image2 = mock(ImageFile.class);

        List<ImageFile> images = new ArrayList<>();
        images.add(image1);
        images.add(image2);

        Product fakeProduct = new Product();
        fakeProduct.setId(fakeProductId);
        fakeProduct.addImage(image1);
        fakeProduct.addImage(image2);

        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        fakeCustomer.addProduct(fakeProduct);

        when(productService.getProductById(fakeProductId)).thenReturn(fakeProduct);
        when(customerService.findById(fakeCustomerId)).thenReturn(fakeCustomer);

        mvc.perform(get(REQ_MAP + fakeCustomerId + "/product/" + fakeProductId + "/images"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", fakeCustomer))
                .andExpect(model().attribute("product", fakeProduct))
                .andExpect(model().attribute("images", images))
                .andExpect(model().size(3))
                .andExpect(view().name("product/images"));

        verify(productService, times(1)).getProductById(fakeProductId);
        verify(customerService, times(1)).findById(fakeCustomerId);

    }

    @Test
    void testDeleteProduct() throws Exception {

        Integer fakeCustomerId = 99;
        Integer fakeProductId = 88;

        Customer customer = new Customer();


        when(customerService.findById(fakeCustomerId)).thenReturn(customer);

        mvc.perform(post(REQ_MAP + fakeCustomerId + "/product/" + fakeProductId + "/delete"))
                .andExpect(status().is3xxRedirection());

        verify(productService, times(1)).deleteProduct(fakeCustomerId, fakeProductId);
    }


}
