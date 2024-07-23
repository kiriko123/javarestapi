package com.example.shopapp.controller;

import com.example.shopapp.configuration.Translator;
import com.example.shopapp.dto.request.OrderDetailRequestDTO;
import com.example.shopapp.dto.response.OrderDetailResponse;
import com.example.shopapp.dto.response.ResponseData;
import com.example.shopapp.service.OrderDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
@Validated
@Slf4j
@Tag(name = "Order_detail controller")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping("/")
    public ResponseData<?> createOrderDetail(@Valid @RequestBody OrderDetailRequestDTO orderDetailRequestDTO){
        return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("order_details.add.success"),
                orderDetailService.createOrderDetail(orderDetailRequestDTO).getId());
    }
    @GetMapping("/{id}")
    public ResponseData<?> getOrderDetail(@PathVariable("id") Long id){
        return new ResponseData<>(HttpStatus.OK.value(),
                Translator.toLocale("order_details.get.success"),
                OrderDetailResponse.fromOrderDetail(orderDetailService.getOrderDetailById(id)));
    }
    @GetMapping("/order/{orderId}")
    public ResponseData<?> getOrderDetailByOrderId(@Min(1) @PathVariable("orderId") long orderId){
        return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("order_details.get.success")
                ,orderDetailService.getAllOrderDetailsByOrderId(orderId).stream().map(OrderDetailResponse::fromOrderDetail).toList());
    }
    @PutMapping("/{id}")
    public ResponseData<?> updateOrderDetail(@Min(1) @PathVariable("id") long id,
                                             @Valid @RequestBody OrderDetailRequestDTO orderDetailRequestDTO){
        orderDetailService.updateOrderDetail(id, orderDetailRequestDTO);
        return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("order_details.upd.success"));
    }
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteOrderDetail(@Min(1) @PathVariable("id") long id){
        orderDetailService.deleteOrderDetail(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("order_details.del.success"));
    }
}
