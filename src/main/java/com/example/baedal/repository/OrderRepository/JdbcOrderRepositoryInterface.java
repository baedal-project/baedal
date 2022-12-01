package com.example.baedal.repository.OrderRepository;

import com.example.baedal.domain.Orders;

import java.util.List;

public interface JdbcOrderRepositoryInterface {

    void saveAll(List<Orders> ordersList);
}
