package com.example.baedal.controller;

import com.example.baedal.dto.request.OrderCheckRequestDto;
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
    @PostMapping(value = "/api/v1/orders")
    public ResponseDto<?> postOrder(@RequestBody OrderRequestDto requestDto, HttpServletRequest request) {
        return orderService.postOrder(requestDto, request);
    }

    @PostMapping(value = "/api/v2/orders")
    public ResponseDto<?> postOrderWithPessimisticWrite(@RequestBody OrderRequestDto requestDto, HttpServletRequest request) {
        return orderService.postOrderWithPessimisticWrite(requestDto, request);
    }

    @PostMapping(value = "/api/v3/orders")
    public ResponseDto<?> postOrderWithPessimisticRead(@RequestBody OrderRequestDto requestDto, HttpServletRequest request) {
        return orderService.postOrderWithPessimisticRead(requestDto, request);
    }

    //=============================================================================================================

    //주문 전체조회
    //@LogExecutionTime
    @GetMapping(value = "/api/v1/orders")
    public ResponseDto<?> getAllOrderWithJPA(HttpServletRequest request) {
        return orderService.getAllOrderWithJPA(request);
    }

    @GetMapping(value = "/api/v2/orders")
    public ResponseDto<?> getAllOrderWithQuerydsl(HttpServletRequest request) {
        return orderService.getAllOrderWithQuerydsl(request);
    }

    @GetMapping(value = "/api/v3/orders")
    public ResponseDto<?> getAllOrderWithPaging(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                HttpServletRequest request) {
        return orderService.getAllOrderWithPaging(pageable,request);
    }

    //queryDsl 없이 paging 처리
    @GetMapping (value = "api/v4/orders")
    public ResponseDto<?> getAllOrderWithJPAPaging(@PageableDefault(page = 0, size = 10)Pageable pageable,
                                      HttpServletRequest request) {
        return orderService.getAllOrdersWithJPAPaging(pageable,request);
    }

    //consumer or producer
    @PostMapping (value = "api/orders")
    public ResponseDto<?> getAllOrderByConsumerProducer(@PageableDefault(page = 0, size = 10)Pageable pageable,
                                      HttpServletRequest request, @RequestBody OrderCheckRequestDto orderCheckRequestDto) {
        return orderService.getAllOrdersByConsumerProducer(pageable,request,orderCheckRequestDto);
    }


    //주문 상세 조회
    //@LogExecutionTime
    @GetMapping(value = "/api/orders/{orderId}")
    public ResponseDto<?> getOneOrder(@PathVariable Long orderId,HttpServletRequest request) {

        return orderService.getOneOrder(orderId,request);
    }

}
