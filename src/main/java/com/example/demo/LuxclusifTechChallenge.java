package com.example.demo;

import com.example.demo.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.File;

@SpringBootApplication
public class LuxclusifTechChallenge {

	public static void main(String[] args) {
		new File(ProductService.UPLOAD_DIR).mkdir();
		SpringApplication.run(LuxclusifTechChallenge.class, args);
	}

}
