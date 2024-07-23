package com.example.shopapp.service.impl;

import com.example.shopapp.dto.request.CategoryRequestDTO;
import com.example.shopapp.exception.InvalidDataException;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.model.Category;
import com.example.shopapp.repository.CategoryRepository;
import com.example.shopapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequestDTO categoryRequestDTO) {
        if(categoryRepository.existsByName(categoryRequestDTO.getName())){
            throw new InvalidDataException("Da ton tai category co name tren");
        }
        Category category = Category.builder()
                .name(categoryRequestDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Khong co category tren"));
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public void updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category category = getCategoryById(id);
        category.setName(categoryRequestDTO.getName());
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        //xoa cung
        categoryRepository.deleteById(id);
    }
}
