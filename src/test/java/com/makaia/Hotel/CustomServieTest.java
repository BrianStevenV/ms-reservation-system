package com.makaia.Hotel;

import com.makaia.Hotel.Exceptions.HandlerResponseException;
import com.makaia.Hotel.modules.Customer;
import com.makaia.Hotel.repositories.CustomerRepository;
import com.makaia.Hotel.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

public class CustomServieTest {
    CustomerRepository customerRepository;
    CustomerService customerService;

    @Before
    public void setUp(){
        this.customerRepository = mock(CustomerRepository.class);
        this.customerService = new CustomerService(customerRepository);
    }

    @Test
    public void validateCustomer() {
        Customer customer = new Customer(1, "Pepito", "Peréz", "Calle falsa 123", 18, "peto@example.com");
        Customer custom = customerService.create(customer);

        assertTrue((custom.getFirstName() != null && custom.getLastName() != null && custom.getDni() instanceof Integer));
    }

    @Test(expected = HandlerResponseException.class)
    public void validationDNI(){
        //Arrange
        Customer customer = new Customer(null, "Brian Test", "Steven Test", "Cr. Java", 20, "test@java.org");
        Customer response = this.customerService.create(customer);
        //Act and Assert
        assertThrows(ResponseStatusException.class, () -> {
            customerService.create(customer);
        });

    }

    @Test
    public void testResearchByIdSuccess(){
        // Arrange
        int dni = 5;
        Customer customer = new Customer(dni, "John", "Doe", "123 Main St.", 30, "johndoe@example.com");
        when(customerRepository.findById(dni)).thenReturn(Optional.of(customer));

        // Act
        Optional<Customer> result = customerService.researchById(dni);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(dni, result.get().getDni().intValue());
        assertEquals(customer.getFirstName(), result.get().getFirstName());
        assertEquals(customer.getLastName(), result.get().getLastName());
        assertEquals(customer.getAddress(), result.get().getAddress());
        assertEquals(customer.getAge(), result.get().getAge());
        assertEquals(customer.getEmail(), result.get().getEmail());
    }

    @Test
    public void testResearchAllSuccess() {
        // Arrange
        Customer customer1 = new Customer(1, "John", "Doe", "123 Main St.", 30, "johndoe@example.com");
        Customer customer2 = new Customer(2, "Jane", "Smith", "456 Park Ave.", 25, "janesmith@example.com");
        List<Customer> customerList = Arrays.asList(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customerList);

        // Act
        List<Customer> result = customerService.researchAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(customer1, result.get(0));
        assertEquals(customer2, result.get(1));
    }

    @Test
    public void testResearchAllWithCustomAvailable() {
        // Arrange
        List<Customer> customerList = Arrays.asList(
                new Customer(1, "John", "Doe", "123 Main St.", 30, "johndoe@example.com"),
                new Customer(2, "Juana", "Welp", "125 Double St.", 30, "juana@example.com"),
                new Customer(3, "Maria", "Gonzales", "323 Set St.", 30, "jmariae@example.com")
        );
        when(customerRepository.findAll()).thenReturn(customerList);

        // Act
        List<Customer> result = customerService.researchAll();

        // Assert
        assertEquals(customerList, result);
    }

    @Test
    public void testResearchByIdWithCustomAvailable() {
        // Arrange
        int dni = 1;
        Customer customer = new Customer(dni,"John", "Doe", "123 Main St.", 30, "johndoe@example.com");
        when(customerRepository.findById(dni)).thenReturn(Optional.of(customer));

        // Act
        Optional<Customer> result = customerService.researchById(dni);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(customer, result.get());
    }

    @Test
    public void testResearchAllWithoutCustomAvailable() {
        // Arrange
        List<Customer> customerList = Collections.emptyList();
        when(customerRepository.findAll()).thenReturn(customerList);

        // Act and Assert
        HandlerResponseException exception = assertThrows(HandlerResponseException.class, () -> {
            customerService.researchAll();
        });

        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus()),
                () -> assertEquals("Database is empty.", exception.getReason())
        );
    }

}
