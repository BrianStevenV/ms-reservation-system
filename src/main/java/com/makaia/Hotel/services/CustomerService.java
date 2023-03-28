package com.makaia.Hotel.services;

import com.makaia.Hotel.Exceptions.HandlerResponseException;
import com.makaia.Hotel.modules.Customer;
import com.makaia.Hotel.modules.Room;
import com.makaia.Hotel.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(Customer customer){
        //Exceptions
        if(customer.getDni() != null ){
            Optional<Customer> tempCustomer = this.customerRepository.findById(customer.getDni());
            if(tempCustomer.isPresent()){
                throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"DNI is rejected in database.");
            }
        }

        if(customer.getFirstName() != null && customer.getLastName() != null && customer.getDni() instanceof Integer){
            this.customerRepository.save(customer);
            return customer;
        }   else{
            throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"DNI, First Name and Last Name are required");
        }
    }

    public Optional<Customer> researchById(int id){
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()){

            throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"Customer doesn't find in our database.");
        }
        return customer;
    }
    public List<Customer> researchAll(){
        List<Customer> customerList = (List<Customer>) customerRepository.findAll();
        if(customerList.isEmpty()){

            throw new HandlerResponseException(HttpStatus.INTERNAL_SERVER_ERROR,"Database is empty.");
        }
        return customerList;
    }
}