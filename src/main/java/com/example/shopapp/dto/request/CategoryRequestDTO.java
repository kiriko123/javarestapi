package com.example.shopapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class CategoryRequestDTO implements Serializable {
    @NotBlank(message = "Ko cho phep de trong ne")
    private String name;
}
