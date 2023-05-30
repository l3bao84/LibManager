package com.example.LibManager.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name = "borrow")
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {

    @Id
    @Column(name = "borrowID")
    private String borrowID;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerID", nullable = false, referencedColumnName = "customerID")
    @JsonManagedReference
    private Customer customer;

    @OneToMany(mappedBy = "borrow",fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Borrow_Book> borrow_books;


    public Borrow(String notes) {
        this.notes = notes;
    }

    public String getBorrowID() {
        return borrowID;
    }

    public void setBorrowID(String borrowID) {
        this.borrowID = borrowID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Borrow_Book> getBorrow_books() {
        return borrow_books;
    }

    public void setBorrow_books(Set<Borrow_Book> borrow_books) {
        this.borrow_books = borrow_books;
    }
}
