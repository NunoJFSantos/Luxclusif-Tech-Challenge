package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String brand;

    @Pattern(regexp = "^[0-9]*$", message = "Invalid characters")
    @Size(min = 4, max = 4)
    private String yearOfPurchase;

    @NotBlank
    private String productCondition;

    private double price;

    @OneToMany(
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            mappedBy = "product",
            fetch = FetchType.EAGER
    )
    private List<ImageFile> images = new ArrayList<>();

    @ManyToOne
    private Customer customer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getYearOfPurchase() {
        return yearOfPurchase;
    }

    public void setYearOfPurchase(String yearOfPurchase) {
        this.yearOfPurchase = yearOfPurchase;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ImageFile> getImages() {
        return images;
    }

    public void addImage(ImageFile image) {
        images.add(image);
        image.setProduct(this);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", yearOfPurchase='" + yearOfPurchase + '\'' +
                ", productCondition='" + productCondition + '\'' +
                ", price=" + price + "$" +
                '}';
    }
}
