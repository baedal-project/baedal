package com.example.baedal.repository.OrderRepository;

import com.example.baedal.domain.OrderHasItem;
import com.example.baedal.domain.Orders;
import com.example.baedal.dto.response.AllOrderResponseDto;

import java.util.List;

public interface OrderRepositoryCustom {

    //List<AllOrderResponseDto> getAllOrder();
    //List<OrderHasItem> getAllOrder();
    List<Orders> getAllOrder();

    List<AllOrderResponseDto> getOneOrder(Long id);
}
