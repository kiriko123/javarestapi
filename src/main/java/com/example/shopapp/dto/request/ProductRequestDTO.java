package com.example.shopapp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Builder
public class ProductRequestDTO implements Serializable {
    @NotBlank(message = "Name must be not blank")
    @Size(min = 3, max = 200, message = "Name must be bw 3 and 200")
    private String name;
    @Min(value = 0, message = "Price must be >= 0")
    private float price;
    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;

}
