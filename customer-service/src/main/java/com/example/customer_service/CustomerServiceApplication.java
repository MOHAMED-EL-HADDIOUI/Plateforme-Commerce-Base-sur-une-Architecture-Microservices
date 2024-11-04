package com.example.customer_service;

import com.example.customer_service.dto.addressDto.AddressRequestDto;
import com.example.customer_service.dto.customerDto.CustomerRequestDto;
import com.example.customer_service.enums.Country;
import com.example.customer_service.model.Address;
import com.example.customer_service.model.Customer;
import com.example.customer_service.repository.AddressRepository;
import com.example.customer_service.repository.CustomerRepository;
import com.example.customer_service.service.AddressService;
import com.example.customer_service.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}
    @Bean
    CommandLineRunner commandLineRunner(AddressRepository addressRepository, CustomerRepository customerRepository) {
        return args -> {
            for (int i = 1; i <= 10; i++) {
                System.out.println("customer " + i);
                List<Address> addressList = new ArrayList<>();

                for (int j = 1; j <= 3; j++) {
                    System.out.println("customer " + i + " address " + j);

                    Address address = new Address();
                    address.setCity("Paris " + j);
                    address.setCreatedDate(LocalDateTime.now());
                    address.setCountry(Country.FRANCE);
                    address.setDistrict("scjn " + j);
                    address.setDescription("no description");
                    address.setZipCode("7500" + j);

                    // Add address to the list
                    addressList.add(address);
                }

                // Set the address list for the customer
                Customer customer = new Customer();
                customer.setEmail("mohamed" + i + "@gmail.com");
                customer.setCreatedDate(LocalDateTime.now());
                customer.setFirstName("Mohamed " + i);
                customer.setLastName("El Haddadi " + i);
                customer.setPhoneNumber("062156237" + i);
                customer.setAddressList(addressList);

                // Save the customer, cascading the save to the addresses
                Customer savedCustomer = customerRepository.save(customer);
            }
        };
    }


}

