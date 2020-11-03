package com.example.demo.controller.rest;

import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/luxclusif/customer")
public class RestProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @GetMapping(path = "product")
    public List<Product> listProducts() {
        return productService.listAllProducts();
    }

    @GetMapping(path = "product/{id}")
    public ResponseEntity<Product> showProduct(@PathVariable("id") Integer id) {

        if (productService.getProductById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping(path = "{cid}/product")
    public ResponseEntity<List<Product>> listCustomerProducts(@PathVariable("cid") Integer cid) {

        Customer customer = customerService.findById(cid);

        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Product> products = customer.getProducts();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping(path = "{cid}/product")
    public ResponseEntity<?> addProduct(@PathVariable("cid") Integer cid, @Valid @RequestBody Product product, @RequestParam("files") MultipartFile[] files, UriComponentsBuilder uriComponentsBuilder) {

        Customer customer = customerService.findById(cid);

        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Product newProduct = productService.addProduct(cid, product, files);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/luxclusif/customer/" + cid + "product/").build();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = "{cid}/product/{pid}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> updateProduct(@PathVariable("cid") Integer cid, @PathVariable("pid") Integer pid, @Valid @RequestBody Product product, BindingResult bindingResult, @RequestParam MultipartFile[] files) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (productService.getProductById(pid) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (product.getId() != null && !product.getId().equals(pid)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        productService.updateProduct(cid, product, files);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{cid}/product/{pid}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Integer cid, @PathVariable Integer pid) {

        if (productService.getProductById(pid) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productService.deleteProduct(cid, pid);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
