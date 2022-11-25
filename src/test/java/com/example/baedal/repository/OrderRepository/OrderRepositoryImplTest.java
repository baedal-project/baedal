package com.example.baedal.repository.OrderRepository;

import com.example.baedal.domain.Orders;
import com.example.baedal.dto.response.AllOrderResponseDto;
import com.example.baedal.dto.response.QAllOrderResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static com.example.baedal.domain.QOrderHasItem.orderHasItem;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("order 관련 test")
class OrderRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private OrderRepository orderRepository;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("전체 주문개수 맞나 확인")
    public void getAllOrder(){
        //test cases들은 구현체와 최대한 결합도가 낮아야 good
        List<AllOrderResponseDto> allOrders = orderRepository.getAllOrder();
        assertThat(allOrders.size()).isEqualTo(12);
    }

    @Test
    @DisplayName("주문 낱개로 잘 갖고 오나 확인")
    public void getOneOrder(){
        List<AllOrderResponseDto> oneOrder = orderRepository.getOneOrder(1L);
        assertThat(oneOrder.get(0).getAmount().equals(5));
        assertThat(oneOrder.get(0).getStoreId().equals(1L));
        assertThat(oneOrder.get(0).getStoreName().equals("명동왕족"));
        assertThat(oneOrder.get(0).getItemName().equals("족발"));
        assertThat(oneOrder.get(0).getMemberId().equals(1L));
        assertThat(oneOrder.get(0).getMemberName().equals("yeongmin1"));

    }



}