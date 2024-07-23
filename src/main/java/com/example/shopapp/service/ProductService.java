package com.example.shopapp.service;

import com.example.shopapp.dto.request.ProductImageRequestDTO;
import com.example.shopapp.dto.request.ProductRequestDTO;
import com.example.shopapp.dto.response.PageResponse;
import com.example.shopapp.dto.response.ProductResponse;
import com.example.shopapp.model.Product;
import com.example.shopapp.model.ProductsImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {
    Product createProduct(ProductRequestDTO productRequestDTO);
    Product getProductById(Long id);
    PageResponse<?> getAllProduct(PageRequest pageRequest);
    void updateProduct(Long id, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long id);
    boolean existsByName(String name);
    ProductsImage createProductImage(Long productId, ProductImageRequestDTO productImageDTO);
}
