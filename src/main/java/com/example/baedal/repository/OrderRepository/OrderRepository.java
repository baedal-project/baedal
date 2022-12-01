package com.example.baedal.repository.OrderRepository;
import com.example.baedal.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.transaction.Transactional;
import java.util.List;


public interface OrderRepository extends JpaRepository<Orders, Long>, OrderRepositoryCustom {

}
