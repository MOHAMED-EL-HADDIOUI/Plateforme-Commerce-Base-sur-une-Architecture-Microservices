package com.example.product_service;

import com.example.product_service.dto.inventoryDto.InventoryRequestDto;
import com.example.product_service.dto.productDto.ProductRequestDto;
import com.example.product_service.enums.Category;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(ProductService productService) {
        return args -> {
            for (int i = 1; i <= 10; i++) {
                ProductRequestDto product = new ProductRequestDto();
                product.setDescription("Description "+i);
                product.setName("Product "+i);
                product.setPrice((double) (10*i));
                product.setCategory(Category.MODE);
                InventoryRequestDto inventoryRequestDto = new InventoryRequestDto();
                inventoryRequestDto.setStockQuantity(i);
                product.setInventoryRequestDto(inventoryRequestDto);
                productService.createProduct(product);
            }
        };
    }

}
