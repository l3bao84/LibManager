package com.example.LibManager.repositories;

import com.example.LibManager.models.Book;
import com.example.LibManager.models.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book,String> {
    Iterable<Book> findByCategory(Category category);

}
