package com.example.baedal.repository.OrderRepository;

import com.example.baedal.domain.*;
import com.example.baedal.dto.response.AllOrderResponseDto;
import com.example.baedal.dto.response.QAllOrderResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.baedal.domain.QItem.*;
import static com.example.baedal.domain.QMember.*;
import static com.example.baedal.domain.QOrderHasItem.*;
import static com.example.baedal.domain.QOrders.*;
import static com.example.baedal.domain.QStore.*;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

//    @Override

//    before refactoring
    public List<OrderHasItem> getAllOrder(){
        List<OrderHasItem> orderHasItems = queryFactory
                .selectFrom(orderHasItem)
                .join(orderHasItem.item).on(orderHasItem.item.itemId.eq(item.itemId))
                .join(orderHasItem.orders).on(orderHasItem.orders.ordersId.eq(orders.ordersId))
                .join(orders.member).on(orderHasItem.orders.member.memberId.eq(member.memberId))
                .join(item.store).on(orderHasItem.item.store.storeId.eq(store.storeId))
                .fetch();
        return orderHasItems;

    //after refactoring
//    @Override
//    public List<Orders> getAllOrder(){
//        List<Orders>
    }

    @Override
    public List<AllOrderResponseDto> getOneOrder(Long id) {

        return queryFactory
                .select(new QAllOrderResponseDto(orderHasItem.item.store.storeId,orderHasItem.item.store.name,
                        orderHasItem.item.itemId, orderHasItem.item.name,orderHasItem.amount,
                        orderHasItem.item.price, orderHasItem.orders.member.memberId,orderHasItem.orders.member.name,
                        orderHasItem.orders.createdAt))
                //.select(//연관관계 -> DTO에서 변형 List로 넣자)
                .from(orderHasItem)
                .leftJoin(orderHasItem.orders)
                .leftJoin(orderHasItem.item)
                .where(orderHasItem.orders.ordersId.eq(id))
                .fetch();

    }


}
