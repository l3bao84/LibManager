package com.example.LibManager.repositories;

import com.example.LibManager.models.PlCompany;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlCompanyRepository extends CrudRepository<PlCompany, String> {

    Optional<PlCompany> findByPlCompanyName(String name);
}
