package com.example.shopapp.service;

import com.example.shopapp.dto.request.CategoryRequestDTO;
import com.example.shopapp.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequestDTO categoryRequestDTO);

    Category getCategoryById(Long id);

    List<Category> getAllCategory();

    void updateCategory(Long id, CategoryRequestDTO categoryRequestDTO);

    void deleteCategory(Long id);
}
