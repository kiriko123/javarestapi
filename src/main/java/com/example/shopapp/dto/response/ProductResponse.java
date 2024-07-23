package com.example.shopapp.dto.response;

import com.example.shopapp.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class ProductResponse implements Serializable {
    private String name;
    private float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("create_at")
    private Date createAt;
    @JsonProperty("update_at")
    private Date updateAt;

    public static ProductResponse fromProduct(Product product){
        return ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .createAt(product.getCreateAt())
                .updateAt(product.getUpdateAt())
                .build();
    }
}
