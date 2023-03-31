package com.example.LibManager.repositories;

import com.example.LibManager.models.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book,String> {
    Iterable<Book> findByCategoryID(String categoryID);
}
