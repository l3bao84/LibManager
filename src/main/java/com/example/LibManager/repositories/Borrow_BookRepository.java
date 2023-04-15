package com.example.LibManager.repositories;

import com.example.LibManager.controller.Borrow_BookController;
import com.example.LibManager.models.BorrowBookKey;
import com.example.LibManager.models.Borrow_Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Borrow_BookRepository extends JpaRepository<Borrow_Book, String> {

}
