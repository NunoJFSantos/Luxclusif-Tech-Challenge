package com.example.demo.service;

import com.example.demo.dao.CustomerDao;
import com.example.demo.dao.ImageFileDao;
import com.example.demo.dao.ProductDao;
import com.example.demo.model.Customer;
import com.example.demo.model.ImageFile;
import com.example.demo.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ProductService.class})
public class ProductServiceTest {

    @MockBean
    private ProductDao productDao;

    @MockBean
    private CustomerDao customerDao;

    @MockBean
    private ImageFileDao imageFileDao;

    @Test
    void testListAllProducts() {

        List<Product> fakeList = mock(ArrayList.class);

        when(productDao.findAll()).thenReturn(fakeList);

        List<Product> products = productDao.findAll();

        assertNotNull(products);
        verify(productDao, times(1)).findAll();
    }

    @Test
    void testGetProductById() {

        Integer fakeId = 999;

        Product fakeProduct = new Product();

        Optional<Product> optionalProduct = Optional.ofNullable(fakeProduct);

        when(productDao.findById(fakeId)).thenReturn(optionalProduct);

        Product product = productDao.findById(fakeId).orElse(null);

        assertEquals(product, fakeProduct);
        verify(productDao, times(1)).findById(fakeId);
    }

    @Test
    void testAddProduct() {

        Product fakeProduct = mock(Product.class);

        when(productDao.save(fakeProduct)).thenReturn(fakeProduct);

        Product product = productDao.save(fakeProduct);

        assertNotNull(product);
        verify(productDao, times(1)).save(fakeProduct);
    }

    @Test
    void testUpdateProduct() {

        Integer fakeProductId = 999;
        Integer fakeCustomerId = 888;

        Customer fakeCustomer = spy(Customer.class);
        Optional<Customer> optionalCustomer = Optional.ofNullable(fakeCustomer);
        ImageFile imageFile = mock(ImageFile.class);

        Product fakeProduct = new Product();
        fakeProduct.setId(fakeProductId);
        fakeProduct.setBrand("Brand");
        fakeProduct.setPrice(999.99);
        fakeProduct.setProductCondition("Bad");
        fakeProduct.setYearOfPurchase("1999");
        fakeProduct.addImage(imageFile);
        fakeProduct.setCustomer(fakeCustomer);

        when(productDao.save(fakeProduct)).thenReturn(fakeProduct);
        when(customerDao.findById(fakeCustomerId)).thenReturn(optionalCustomer);

        Product product = productDao.save(fakeProduct);

        Customer customer = customerDao.findById(fakeCustomerId).orElse(null);
        customer.addProduct(product);

        when(customerDao.save(customer)).thenReturn(customer);

        customerDao.save(customer);

        assertEquals(product, fakeProduct);
        assertTrue(customer.getProducts().contains(product));
        verify(productDao, times(1)).save(fakeProduct);
        verify(customerDao, times(1)).findById(fakeCustomerId);
        verify(customerDao, times(1)).save(customer);
    }

    @Test
    void testDeleteProduct() {

        Integer fakeId = 999;

        productDao.deleteById(fakeId);

        verify(productDao, times(1)).deleteById(fakeId);
    }

    @Test
    void testSaveFile() {

        String fakeFileName = "customer88-product99-image2.png";

        ImageFile fakeImage = new ImageFile();
        fakeImage.setFileName(fakeFileName);

        when(imageFileDao.save(fakeImage)).thenReturn(fakeImage);

        ImageFile imageFile = imageFileDao.save(fakeImage);

        assertEquals(imageFile, fakeImage);
        verify(imageFileDao, times(1)).save(fakeImage);
    }
}
