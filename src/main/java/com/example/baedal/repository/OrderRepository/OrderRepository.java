package com.example.baedal.repository.OrderRepository;
import com.example.baedal.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Orders, Long>, OrderRepositoryCustom {

}
