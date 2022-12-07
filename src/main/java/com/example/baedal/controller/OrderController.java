package com.example.baedal.controller;

import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문
    //@LogExecutionTime
    @PostMapping(value = "/api/orders")
    public ResponseDto<?> postOrder(@RequestBody OrderRequestDto requestDto, HttpServletRequest request) {
        return orderService.postOrder(requestDto, request);
    }

    //주문 전체조회
    //@LogExecutionTime
    @GetMapping(value = "/api/orders")
    public ResponseDto<?> getAllOrder() {
        return orderService.getAllOrder();
    }

    //주문 전체 조회 v2
//    @GetMapping(value = "/api/v2/orders")
//    public ResponseDto<?> getAllOrderWithPaging(
//            @RequestParam(value = "offset", defaultValue = "0") int offset,
//            @RequestParam(value = "limit", defaultValue = "10") int limit,
//            ) {
//        return orderService.getAllOrderWithPaging(pageable);
//    }

    @GetMapping(value = "/api/v2/orders")
    public ResponseDto<?> getAllOrderWithPaging(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return orderService.getAllOrderWithPaging(pageable);
    }

    //queryDsl 없이 paging 처리
    @GetMapping (value = "api/v3/orders")
    public ResponseDto<?> getAllStore(@PageableDefault(page = 0, size = 10)Pageable pageable) {
        return orderService.getAllOrdersWithJPAPaging(pageable);
    }

    //주문 상세 조회
    //@LogExecutionTime
    @GetMapping(value = "/api/orders/{orderId}")
    public ResponseDto<?> getOrder(@PathVariable Long orderId){

        return orderService.getOneOrder(orderId);
    }

}
