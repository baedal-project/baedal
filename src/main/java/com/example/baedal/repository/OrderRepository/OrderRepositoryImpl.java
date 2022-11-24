package com.example.baedal.repository.OrderRepository;

import com.example.baedal.dto.response.AllOrderResponseDto;
import com.example.baedal.dto.response.QAllOrderResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.baedal.domain.QOrderHasItem.*;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AllOrderResponseDto> getAllOrder(){
        return queryFactory
                .select(new QAllOrderResponseDto(orderHasItem.item.store.storeId,orderHasItem.item.store.name,
                        orderHasItem.item.itemId, orderHasItem.item.name,orderHasItem.amount,
                        orderHasItem.item.price, orderHasItem.orders.member.memberId,orderHasItem.orders.member.name,
                        orderHasItem.orders.createdAt))
                .from(orderHasItem)
                .leftJoin(orderHasItem.orders)
                .leftJoin(orderHasItem.item)
                .orderBy(orderHasItem.orderHasItemId.asc())
                .fetch();
    }

    @Override
    public List<AllOrderResponseDto> getOneOrder(Long id) {

        return queryFactory
                .select(new QAllOrderResponseDto(orderHasItem.item.store.storeId,orderHasItem.item.store.name,
                        orderHasItem.item.itemId, orderHasItem.item.name,orderHasItem.amount,
                        orderHasItem.item.price, orderHasItem.orders.member.memberId,orderHasItem.orders.member.name,
                        orderHasItem.orders.createdAt))
                .from(orderHasItem)
                .leftJoin(orderHasItem.orders)
                .leftJoin(orderHasItem.item)
                .where(orderHasItem.orders.ordersId.eq(id))
                .fetch();

    }


}
