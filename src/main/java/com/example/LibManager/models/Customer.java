package com.example.LibManager.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(generator = "customerGenerator")
    @GenericGenerator(name = "customerGenerator", strategy = "com.example.LibManager.Generator.CustomerIDGenerator")
    @Column(name = "customerID")
    private String customerID;

    @Column(name = "customerName")
    @NotEmpty(message = "Tên khách hàng không được để trống")
    private String customerName;

    @Column(name = "phoneNumber")
    @NotEmpty(message = "SĐT không được để trống")
    private String phoneNumber;

    @Column(name = "customerLocation")
    @NotEmpty(message = "Địa chỉ khách hàng không được để trống")
    private String customerLocation;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Borrow> borrows;

    public Customer() {}

    public Customer(String customerName, String phoneNumber, String customerLocation, Set<Borrow> borrows) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.customerLocation = customerLocation;
        this.borrows = borrows;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public Set<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(Set<Borrow> borrows) {
        this.borrows = borrows;
    }
}
