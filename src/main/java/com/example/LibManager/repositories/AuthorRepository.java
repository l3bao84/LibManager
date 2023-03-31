package com.example.LibManager.repositories;

import com.example.LibManager.models.Author;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, String> {
    @Override
    Optional<Author> findById(String s);
}
