package com.example.LibManager.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name = "plCompany")
public class PlCompany {

    @Id
    @GeneratedValue(generator = "plCompanyGenerator")
    @GenericGenerator(name = "plCompanyGenerator", strategy = "com.example.LibManager.Generator.PlCompanyIDGenerator")
    @Column(name = "plCompanyID")
    private String plCompanyID;

    @Column(name = "plCompanyName")
    private String plCompanyName;

    @OneToMany(mappedBy = "plCompany")
    @JsonManagedReference
    private Set<Book> books;

    public PlCompany() {

    }

    public PlCompany(String plCompanyID, String plCompanyName) {
        this.plCompanyID = plCompanyID;
        this.plCompanyName = plCompanyName;
    }

    public String getPlCompanyID() {
        return plCompanyID;
    }

    public void setPlCompanyID(String plCompanyID) {
        this.plCompanyID = plCompanyID;
    }

    public String getPlCompanyName() {
        return plCompanyName;
    }

    public void setPlCompanyName(String plCompanyName) {
        this.plCompanyName = plCompanyName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
