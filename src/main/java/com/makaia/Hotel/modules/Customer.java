package com.makaia.Hotel.modules;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    @Id
    private Integer dni;
    @Column(name = "firstName", length = 50)
    private String firstName;
    @Column(name = "lastName", length = 50)
    private String lastName;
    @Column(name = "address", length = 100)
    private String address;
    @Column(name = "age")
    private Integer age;
    @Column(name = "email")
    private String email;

    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "customer")
    @JsonIgnoreProperties("customer")
    private List<Reservation> reservations;

    // TODO: delete constructor
    public Customer(Integer dni, String firstName, String lastName, String address, Integer age, String email) {
        this.dni = dni;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.age = age;
        this.email = email;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
