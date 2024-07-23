package com.example.shopapp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class OrderDetailRequestDTO implements Serializable {
    @JsonProperty("order_id")
    @Min(1)
    private Long orderId;

    @JsonProperty("product_id")
    @Min(1)
    private Long productId;

    @Min(0)
    private float price;

    @Min(1)
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Min(0)
    @JsonProperty("total_money")
    private float totalMoney;

    private String color;
}
