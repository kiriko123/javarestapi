package com.example.shopapp.controller;

import com.example.shopapp.configuration.Translator;
import com.example.shopapp.dto.request.CategoryRequestDTO;
import com.example.shopapp.dto.response.ResponseData;
import com.example.shopapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@Validated
@Slf4j
@Tag(name = "Category controller")
@RequiredArgsConstructor
public class  CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get all categories", description = "Api get all categories")
    @GetMapping("/getAllCategories")
    public ResponseData<?> getAllCategories(@RequestParam(required = false, name = "pageNo", defaultValue = "0") int pageNo,
                                            @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        log.info("Get all categories");
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("category.get.success"), categoryService.getAllCategory());
    }

    @Operation(summary = "Create category", description = "Api create category")
    @PostMapping("/")
    public ResponseData<?> addCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        log.info("Create category {}", categoryRequestDTO);
        return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("category.add.success"),
                categoryService.createCategory(categoryRequestDTO).getId());
    }
    @Operation(summary = "Update category", description = "Api update category")
    @PutMapping("/{id}")
    public ResponseData<?> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        log.info("Update category {}", categoryRequestDTO);
        categoryService.updateCategory(id, categoryRequestDTO);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("category.upd.success"));
    }
    @Operation(summary = "Delete category", description = "Api delete category")
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteCategory(@PathVariable Long id) {
        log.info("Delete category where id = {}", id);
        categoryService.deleteCategory(id);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("category.del.success"));
    }
}
