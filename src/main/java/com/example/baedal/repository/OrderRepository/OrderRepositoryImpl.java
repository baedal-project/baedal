package com.example.baedal.repository.OrderRepository;

import com.example.baedal.domain.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.baedal.domain.QItem.*;
import static com.example.baedal.domain.QOrderHasItem.*;
import static com.example.baedal.domain.QOrders.*;
import static com.example.baedal.domain.QStore.store;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;


//    before refactoring
//    public List<OrderHasItem> getAllOrder(){
//        List<OrderHasItem> orderHasItems = queryFactory
//                .selectFrom(orderHasItem)
//                .join(orderHasItem.item).on(orderHasItem.item.itemId.eq(item.itemId))
//                .join(orderHasItem.orders).on(orderHasItem.orders.ordersId.eq(orders.ordersId))
//                .join(orders.member).on(orderHasItem.orders.member.memberId.eq(member.memberId))
//                .join(item.store).on(orderHasItem.item.store.storeId.eq(store.storeId))
//                .fetch();
//        return orderHasItems;

//==========================================refactoring v1=========================
   @Override
    public List<Orders> getAllOrder(){
        //별칭으로 사용할 QType
        QOrderHasItem orderHasItem1 = orderHasItem;
        QItem item1 = item;

        List<Orders> ordersList = queryFactory
                .select(orders)
                .from(orders)
                .join(orders.member).fetchJoin()
                .join(orders.orderHasItems, orderHasItem1).fetchJoin()
                .join(orderHasItem1.item, item1).fetchJoin()
                //.join(item1.store, store).fetchJoin()
                .orderBy(orders.ordersId.asc())
                .distinct()
                .fetch();

        return ordersList;
//==========================================refactoring v2============================


    }

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
    @Override
    public Orders getOneOrder(Long id){
        //별칭으로 사용할 QType
        QOrderHasItem orderHasItem2 = orderHasItem;
        QItem item2 = item;

        Orders order = queryFactory
                .select(orders)
                .from(orders)
                .join(orders.member).fetchJoin()
                .join(orders.orderHasItems, orderHasItem2).fetchJoin()
                .join(orderHasItem2.item, item2).fetchJoin()
                //.join(item2.store, store).fetchJoin()
                .distinct()
                .where(orders.ordersId.eq(id))
                .fetchOne();

        return order;

    }

//    @Override
//    public List<Orders> getAllOrderWithPaging(int offset, int limit, Pageable pageable) {
//        QOrderHasItem orderHasItem3 = orderHasItem;
//        QItem item3 = item;
//
//        List<Orders> ordersListWithMemberOrders = queryFactory
//                .select(orders)
//                .from(orders)
//                .join(orders.member).fetchJoin()
//                //.join(orders.orderHasItems, orderHasItem1).fetchJoin()
//                //.join(orderHasItem1.item, item1).fetchJoin()
//                //.join(item1.store, store).fetchJoin()
//                .orderBy(orders.ordersId.asc())
//                .offset(offset)
//                .limit(limit)
//                .offset(pageable.getNumberOfPages())
//                //.distinct()
//                .fetch();
//
//        return ordersListWithMemberOrders;
//    }

    @Override
    public Page<Orders> getAllOrderWithPaging(Pageable pageable) {
        QOrderHasItem orderHasItem3 = orderHasItem;
        QItem item3 = item;

        List<Orders> ordersListWithMemberOrders = queryFactory
                .select(orders)
                .from(orders)
                .join(orders.member).fetchJoin()
                //.join(orders.orderHasItems, orderHasItem1).fetchJoin()
                //.join(orderHasItem1.item, item1).fetchJoin()
                //.join(item1.store, store).fetchJoin()
                .orderBy(orders.ordersId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(orders.count())
                .from(orders);


        return PageableExecutionUtils.getPage(ordersListWithMemberOrders,pageable,count::fetchOne);
    }

    //memberId에 해당하는 주문내역 조회
    @Override
    public Page<Orders> getOrdersByMemberId(Long memberId, Pageable pageable) {

        List<Orders> ordersListWithConsumer = queryFactory
                .select(orders)
                .from(orders)
                .join(orders.member).fetchJoin()
                .where(orders.member.memberId.eq(memberId))
                .orderBy(orders.ordersId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(orders.count())
                .from(orders);


        return PageableExecutionUtils.getPage(ordersListWithConsumer,pageable,count::fetchOne);

    }

    //storeId에 해당하는 주문내역 조회
    @Override
    public Page<Orders> getOrdersByStoreId(Long storeId, Pageable pageable) {


        //member의 storeId에 따른 주문내역 조회
        List<Orders> ordersListWithProducer = queryFactory
                .select(orders)
                .from(orders)
                .join(orders.member).fetchJoin()
                .join(orders.orderHasItems, orderHasItem).fetchJoin()
                .join(orderHasItem.item, item).fetchJoin()
                .join(item.store, store).fetchJoin()
                .where(store.storeId.eq(storeId))
                .orderBy(orders.ordersId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(orders.count())
                .from(orders);

        return PageableExecutionUtils.getPage(ordersListWithProducer,pageable,count::fetchOne);

    }
}
