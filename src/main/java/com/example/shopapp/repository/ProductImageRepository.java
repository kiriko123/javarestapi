package com.example.shopapp.repository;

import com.example.shopapp.model.ProductsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductsImage, Long> {
    List<ProductsImage> findByProductId(Long productId);
}
