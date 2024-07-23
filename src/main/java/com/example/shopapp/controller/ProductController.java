package com.example.shopapp.controller;

import com.example.shopapp.configuration.Translator;
import com.example.shopapp.dto.request.ProductImageRequestDTO;
import com.example.shopapp.dto.request.ProductRequestDTO;
import com.example.shopapp.dto.response.PageResponse;
import com.example.shopapp.dto.response.ProductResponse;
import com.example.shopapp.dto.response.ResponseData;
import com.example.shopapp.dto.response.ResponseError;
import com.example.shopapp.model.Product;
import com.example.shopapp.model.ProductsImage;
import com.example.shopapp.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Product controller")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public ResponseData<?> getAllProducts(@RequestParam(required = false, name = "pageNo", defaultValue = "0")@Min(0) int pageNo,
                                            @RequestParam(required = false, name = "pageSize", defaultValue = "20")@Min(1) int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("createAt").descending());
        PageResponse<?> productPage = productService.getAllProduct(pageRequest);
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("product.get.success"), productPage);
    }
    @GetMapping("/{productId}")
    public ResponseData<?> getProductById(@PathVariable("productId")
                                              @Min(value = 1, message = "ProductId must be greater than or equal to 1") long productId) {
        Product product = productService.getProductById(productId);
        return new ResponseData<>(HttpStatus.OK.value(), "Products", ProductResponse.fromProduct(product));
    }


    @PostMapping("/")
    public ResponseData<?> addProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO){
        Product product =  productService.createProduct(productRequestDTO);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Product added", product.getId());
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<?> uploadImages(@PathVariable(name = "id") @Min(1) Long id
            ,@RequestPart(name = "files", required = false) List<MultipartFile> files){

        List<MultipartFile> multipartFiles = files;
        multipartFiles = files == null ? new ArrayList<>() : multipartFiles;
        if (multipartFiles.size() > ProductsImage.MAXIMUM_IMAGES_PER_PRODUCT){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Khong dc upload qua 5 anh cho 1 san pham");
        }
        Product existedProduct = productService.getProductById(id);

        List<ProductsImage> productsImages = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            if (file.getSize() == 0) {
                continue;
            }
            if (file.getSize() > 10 * 1024 * 1024) {
                return new ResponseError(HttpStatus.PAYLOAD_TOO_LARGE.value(), "File is too big");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return new ResponseError(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "File must be an image");
            }
            try {
                String fileName = storeImage(file);
                ProductsImage productsImage = productService.createProductImage(existedProduct.getId(),
                        ProductImageRequestDTO.builder()
                                .imageUrl(fileName)
                                .build());
                productsImages.add(productsImage);
            } catch (Exception e) {
                return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }
        return new ResponseData<>(HttpStatus.CREATED.value(), "Product added", productsImages);
    }

    private String storeImage(MultipartFile file) throws IOException {
        if(!isImageFile(file) || file.getOriginalFilename() == null){
            throw new IOException();
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //them uuid vao truoc file de dam bao ten file la duy nhat
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        //duong dan den thu muc chua file
        Path uploadDir = Paths.get("uploads");
        //kiem tra va tao thu muc neu khong ton tai
        if(!Files.exists(uploadDir)){
            Files.createDirectory(uploadDir);
        }
        //duong da day du den file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        //sao chep vo thu muc muc dich
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    private boolean isImageFile(MultipartFile multipartFile){
        String contentType = multipartFile.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @DeleteMapping("/{productId}")
    public ResponseData<?> deleteProduct(@PathVariable("productId")
                                             @Min(value = 1, message = "ProductId must be greater than or equal to 1") long productId) {
        productService.deleteProduct(productId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Product deleted");
    }
    @PutMapping("/{productId}")
    public ResponseData<?> updateProduct(@PathVariable(name = "productId")
                                             @Min(value = 1, message = "ProductId must be greater than or equal to 1") long productId,
                                         @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        productService.updateProduct(productId, productRequestDTO);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "Product updated");
    }
    @PostMapping("/generateFakeProducts")
    public ResponseData<?> generateFakeProducts(){
        Faker faker = new Faker();

        for (int i = 0; i < 50; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 90000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(1, 3))
                    .thumbnail("")
                    .build();
            productService.createProduct(productRequestDTO);
        }

        return new ResponseData<>(HttpStatus.CREATED.value(), "Faker create successfully");
    }
}
