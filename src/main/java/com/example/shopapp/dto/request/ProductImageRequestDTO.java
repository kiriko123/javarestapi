package com.example.shopapp.dto.request;

import com.example.shopapp.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
@Getter
@Builder
public class ProductImageRequestDTO implements Serializable {

    @JsonProperty("product_id")
    @Min(1)
    private Long productId;

    @Size(min = 5, max = 300)
    @JsonProperty("image_url")
    private String imageUrl;
}
