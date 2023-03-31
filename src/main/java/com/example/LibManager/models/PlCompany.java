package com.example.LibManager.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

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
}
