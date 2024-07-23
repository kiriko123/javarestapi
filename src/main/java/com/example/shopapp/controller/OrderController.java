package com.example.shopapp.controller;

import com.example.shopapp.configuration.Translator;
import com.example.shopapp.dto.request.OrderRequestDTO;
import com.example.shopapp.dto.response.ResponseData;
import com.example.shopapp.dto.response.ResponseError;
import com.example.shopapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/orders")
@Validated
@Slf4j
@Tag(name = "Order controller")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/")
    @Operation(summary = "Create order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseData<?> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("order.add.success"),
                orderService.createOrder(orderRequestDTO));
    }

    @GetMapping("/order/{userId}")
    public ResponseData<?> getOrderByUserId(@PathVariable("userId") @Min(1) Long userId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Order found", orderService.getAllOrdersByUserId(userId));
    }

    @GetMapping("/{id}")
    public ResponseData<?> getOrderById(@PathVariable("id") @Min(1) Long orderId) {
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("order.get.success"),
                orderService.getOrderById(orderId));
    }

    @PutMapping("/{id}")
    public ResponseData<?> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        orderService.updateOrder(id, orderRequestDTO);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("order.upd.success"));
    }

    @DeleteMapping("/{id}")
    public ResponseData<?> deleteOrder(@PathVariable("id") Long id) {
        //xoa mem => cap nhat active = false
        orderService.deleteOrder(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("order.del.success"));
    }
}
