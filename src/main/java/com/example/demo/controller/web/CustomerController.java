package com.example.demo.controller.web;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("luxclusif/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {

        model.addAttribute("title", "Sign Up");
        model.addAttribute("customer", new Customer());

        return "customer/add";
    }

    @GetMapping("/{id}")
    public String showCustomerDetails(@PathVariable Integer id, Model model) {

        Customer customer = customerService.findById(id);

        model.addAttribute("customer", customer);
        model.addAttribute("products", customer.getProducts());

        return "customer/details";
    }

    @GetMapping("/{id}/edit")
    public String editCustomer(@PathVariable Integer id, Model model) {

        Customer customer = customerService.findById(id);

        model.addAttribute("customer", customer);

        return "customer/edit";
    }

    @PostMapping("/add")
    public String addCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "customer/add";
        }

        for (Customer c : customerService.getAllCustomers()) {
            if (c.getEmail().equals(customer.getEmail())) {
                model.addAttribute("error", "This email address already exists!");
                return "error";
            }
            if (c.getPhone().equals(customer.getPhone())) {
                model.addAttribute("error", "This phone number already exists!");
                return "error";
            }
        }

        customerService.addCustomer(customer);

        return "redirect:/luxclusif/customer/" + customer.getId();
    }

    @PostMapping("/edit")
    public String updateCustomer(@Valid Customer customer, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "customer/edit";
        }

        customerService.updateCustomer(customer);

        return "redirect:/luxclusif/customer/" + customer.getId();
    }

    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable Integer id) {

        customerService.deleteCustomerById(id);

        return "redirect:/";
    }


}
