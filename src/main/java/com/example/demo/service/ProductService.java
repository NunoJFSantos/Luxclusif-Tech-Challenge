package com.example.demo.service;

import com.example.demo.dao.CustomerDao;
import com.example.demo.dao.ImageFileDao;
import com.example.demo.dao.ProductDao;
import com.example.demo.model.Customer;
import com.example.demo.model.ImageFile;
import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductService {

    public static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ImageFileDao imageFileDao;

    public List<Product> listAllProducts() {
        return productDao.findAll();
    }

    public Product getProductById(Integer id) {
        return productDao.findById(id).orElse(null);
    }

    public Product addProduct(Integer customerId, Product product, MultipartFile[] files) {

        Product newProduct = productDao.save(product);

        for (MultipartFile file : files) {

            ImageFile imageFile = saveFile(customerId, newProduct, file);
            newProduct.addImage(imageFile);
            productDao.save(newProduct);
        }

        Customer customer = customerDao.findById(customerId).orElse(null);
        customer.addProduct(newProduct);

        customerDao.save(customer);

        return newProduct;
    }

    public Product updateProduct(Integer customerId, Product product, MultipartFile[] files) {

        Product updatedProduct = productDao.save(product);

        for (MultipartFile file : files) {

            ImageFile imageFile = saveFile(customerId, updatedProduct, file);
            updatedProduct.addImage(imageFile);
            productDao.save(updatedProduct);
        }

        Customer customer = customerDao.findById(customerId).orElse(null);

        if (customer != null) {
            customer.getProducts().forEach(p -> {
                if (p.getId().equals(product.getId())) {
                    p.setBrand(product.getBrand());
                    p.setPrice(product.getPrice());
                    p.setProductCondition(product.getProductCondition());
                    p.setYearOfPurchase(product.getYearOfPurchase());
                    p.setCustomer(customer);
                }
            });
        }

        customerDao.save(customer);

        return updatedProduct;
    }

    public void deleteProduct(Integer customerId, Integer productId) {

        Customer customer = customerDao.findById(customerId).orElse(null);
        customer.getProducts().removeIf(p -> p.getId().equals(productId));

        productDao.deleteById(productId);
    }

    private ImageFile saveFile(Integer customerId, Product product, MultipartFile file) {

        String[] fileNameSplit = file.getOriginalFilename().split("\\.");

        String fileExtension = fileNameSplit[fileNameSplit.length - 1];

        String newFileName = "customer" + customerId
                + "-product" + product.getId()
                + "-image" + product.getImages().size()
                + "." + fileExtension;

        Path fileNameAndPath = Paths.get(UPLOAD_DIR, newFileName);

        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageFile imageFile = new ImageFile();
        imageFile.setFileName(newFileName);

        return imageFileDao.save(imageFile);
    }
}
