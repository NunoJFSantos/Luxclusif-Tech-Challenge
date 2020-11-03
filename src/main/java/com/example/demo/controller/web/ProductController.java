package com.example.demo.controller.web;

import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("luxclusif/customer")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("product")
    public String listProducts(Model model) {

        model.addAttribute("products", productService.listAllProducts());

        return "product/list";
    }

    @GetMapping("{cid}/product/add")
    public String showProductForm(@PathVariable Integer cid, Model model) {

        if (customerService.findById(cid) == null) {
            model.addAttribute("error", "Customer not found!");
            return "error";
        }

        model.addAttribute("customer", customerService.findById(cid));
        model.addAttribute("product", new Product());

        return "product/add";
    }

    @GetMapping("{cid}/product/{pid}/edit")
    public String editProduct(@PathVariable Integer cid, @PathVariable Integer pid, Model model) {

        Customer customer = customerService.findById(cid);

        Product product = productService.getProductById(pid);

        if (customer == null) {
            model.addAttribute("error", "Customer not found!");
            return "error";
        }

        if (product == null) {
            model.addAttribute("error", "Product not found!");
            return "error";
        }

        if (!customer.getProducts().contains(product)) {
            model.addAttribute("error", "You have no permission to edit this product!");
            return "error";
        }

        model.addAttribute("customer", customer);
        model.addAttribute("product", product);

        return "product/edit";
    }

    @GetMapping("{cid}/product/{pid}/images")
    public String showProductImages(@PathVariable Integer cid, @PathVariable Integer pid, Model model) {

        Product product = productService.getProductById(pid);

        if (product.getImages().isEmpty()) {
            model.addAttribute("error", "This product has no images submitted!");
            return "error";
        }

        model.addAttribute("customer", customerService.findById(cid));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product/images";
    }

    @PostMapping(value = "{cid}/product/add", consumes = {"multipart/form-data"})
    public String addProduct(@PathVariable Integer cid, @Valid Product product, BindingResult bindingResult, @RequestParam("files") MultipartFile[] files) {

        if (bindingResult.hasErrors()) {
            return "error";
        }

        productService.addProduct(cid, product, files);

        return "redirect:/luxclusif/customer/" + cid;
    }

    @PostMapping(value = "{cid}/product/edit", consumes = {"multipart/form-data"})
    public String editProduct(@PathVariable Integer cid, @Valid Product product, BindingResult bindingResult, @RequestParam("files") MultipartFile[] files) {

        if (bindingResult.hasErrors()) {
            return "error";
        }

        productService.updateProduct(cid, product, files);

        return "redirect:/luxclusif/customer/" + cid;
    }

    @PostMapping("{cid}/product/{pid}/delete")
    public String deleteProduct(@PathVariable Integer cid, @PathVariable Integer pid, Model model) {

        Customer customer = customerService.findById(cid);

        if (customer == null) {
            model.addAttribute("error", "Customer not found!");
            return "error";
        }

        productService.deleteProduct(cid, pid);

        return "redirect:/luxclusif/customer/" + cid;
    }
}
