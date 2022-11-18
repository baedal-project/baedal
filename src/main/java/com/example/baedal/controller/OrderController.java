package com.example.baedal.controller;

import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문
    @PostMapping(value = "/api/orders")
    public ResponseDto<?> postOrder(@RequestBody OrderRequestDto requestDto) {
        return orderService.postOrder(requestDto);
    }

    //주문 전체조회
    @GetMapping(value = "/api/orders")
    public ResponseDto<?> getAllOrder() {
        return orderService.getAllOrder();
    }

    //주문 상세 조회
    @GetMapping(value = "/api/orders/{orderId}")
    public ResponseDto<?> getOrder(@PathVariable Long id){
        return orderService.getOneOrder(id);
    }

}
