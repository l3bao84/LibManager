package com.example.LibManager.repositories;

import com.example.LibManager.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, String> {
}
