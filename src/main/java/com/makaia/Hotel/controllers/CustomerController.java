package com.makaia.Hotel.controllers;

import com.makaia.Hotel.modules.Customer;
import com.makaia.Hotel.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

/*    @PostMapping("/customer")
    public ResponseEntity<Customer> register(@RequestBody Customer customer){
        return customerService.create(customer);
    }*/


    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer register(@RequestBody Customer customer){
        return customerService.create(customer);
    }
}