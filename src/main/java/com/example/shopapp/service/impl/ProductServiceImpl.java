package com.example.shopapp.service.impl;

import com.example.shopapp.dto.request.ProductImageRequestDTO;
import com.example.shopapp.dto.request.ProductRequestDTO;
import com.example.shopapp.dto.response.PageResponse;
import com.example.shopapp.dto.response.ProductResponse;
import com.example.shopapp.exception.ResourceNotFoundException;
import com.example.shopapp.model.Category;
import com.example.shopapp.model.Product;
import com.example.shopapp.model.ProductsImage;
import com.example.shopapp.repository.CategoryRepository;
import com.example.shopapp.repository.ProductImageRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.service.CategoryService;
import com.example.shopapp.service.ProductService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO) {
        Category exsitedCategory =  categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Ko co category nay nha"));
        Product product = Product.builder()
                .name(productRequestDTO.getName())
                .price(productRequestDTO.getPrice())
                .thumbnail(productRequestDTO.getThumbnail())
                .category(exsitedCategory)
                .description(productRequestDTO.getDescription())
                .build();

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khong co product tren"));
    }

    @Override
    public PageResponse<?> getAllProduct(PageRequest pageRequest) {
        //lay danh sach san pham theo page va limit
        Page<Product> products = productRepository.findAll(pageRequest);

        /*ProductResponse.builder()
                    .name(p.getName())
                    .description(p.getDescription())
                    .thumbnail(p.getThumbnail())
                    .categoryId(p.getCategory().getId())
                    .price(p.getPrice())
                    .createAt(p.getCreateAt())
                    .updateAt(p.getUpdateAt())
                    .build())*/
        List<ProductResponse> productResponses = products.stream().map(ProductResponse::fromProduct)
                .toList();

        return PageResponse.builder()
                .pageNo(pageRequest.getPageNumber())
                .pageSize(pageRequest.getPageSize())
                .totalPage(products.getTotalPages())
                .items(productResponses)
                .build();
    }

    @Override
    public void updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product product = getProductById(id);

        Category exsitedCategory =  categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Ko co category nay nha"));

        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());
        product.setThumbnail(productRequestDTO.getThumbnail());
        product.setDescription(productRequestDTO.getDescription());
        product.setCategory(exsitedCategory);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        getProductById(id);
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
    @Override
    public ProductsImage createProductImage(Long productId, ProductImageRequestDTO productImageDTO) {
        Product product = getProductById(productId);
        ProductsImage productsImage = ProductsImage.builder()
                .product(product)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //Ko cho insert qua 5 anh cho 1 san pham
        int size = productImageRepository.findByProductId(product.getId()).size();
        if(size >= ProductsImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new RuntimeException();
        }
        return productImageRepository.save(productsImage);
    }
}
