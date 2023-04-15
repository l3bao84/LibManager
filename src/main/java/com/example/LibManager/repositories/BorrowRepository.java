package com.example.LibManager.repositories;

import com.example.LibManager.models.Borrow;
import com.example.LibManager.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowRepository extends JpaRepository<Borrow, String> {
    Optional<Borrow> findByCustomer(Customer customer);
}
