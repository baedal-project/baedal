package com.example.baedal.repository.OrderRepository;

import com.example.baedal.domain.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    //List<AllOrderResponseDto> getAllOrder();
    //List<OrderHasItem> getAllOrder();
    List<Orders> getAllOrder();

    //List<AllOrderResponseDto> getOneOrder(Long id);
    Orders getOneOrder(Long id);

    //    @Override
    //    public List<AllOrderResponseDto> getOneOrder(Long id) {
    //
    //        //before refactoring
    //        return queryFactory
    //                .select(new QAllOrderResponseDto(orderHasItem.item.store.storeId,orderHasItem.item.store.name,
    //                        orderHasItem.item.itemId, orderHasItem.item.name,orderHasItem.amount,
    //                        orderHasItem.item.price, orderHasItem.orders.member.memberId,orderHasItem.orders.member.name,
    //                        orderHasItem.orders.createdAt))
    //                //.select(//연관관계 -> DTO에서 변형 List로 넣자)
    //                .from(orderHasItem)
    //                .leftJoin(orderHasItem.orders)
    //                .leftJoin(orderHasItem.item)
    //                .where(orderHasItem.orders.ordersId.eq(id))
    //                .fetch();
    //
    //    }
    //Orders getOneOrder();

    //    @Override
    //    public List<AllOrderResponseDto> getOneOrder(Long id) {
    //
    //        //before refactoring
    //        return queryFactory
    //                .select(new QAllOrderResponseDto(orderHasItem.item.store.storeId,orderHasItem.item.store.name,
    //                        orderHasItem.item.itemId, orderHasItem.item.name,orderHasItem.amount,
    //                        orderHasItem.item.price, orderHasItem.orders.member.memberId,orderHasItem.orders.member.name,
    //                        orderHasItem.orders.createdAt))
    //                //.select(//연관관계 -> DTO에서 변형 List로 넣자)
    //                .from(orderHasItem)
    //                .leftJoin(orderHasItem.orders)
    //                .leftJoin(orderHasItem.item)
    //                .where(orderHasItem.orders.ordersId.eq(id))
    //                .fetch();
    //
    //    }
//    List<Orders> getAllOrderWithPaging(int offset, int limit, Pageable pageable);
    Page<Orders> getAllOrderWithPaging(Pageable pageable);

    //memberId에 해당하는 주문내역 조회
    Page<Orders> getOrdersByMemberId(Long memberId, Pageable pageable);

    Page<Orders> getOrdersByStoreId(Long storeId, Pageable pageable);
}
