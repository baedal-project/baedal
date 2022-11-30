package com.example.baedal.service;

import com.example.baedal.domain.Item;
import com.example.baedal.domain.OrderHasItem;
import com.example.baedal.domain.Orders;
import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.MemberResponseDto;
import com.example.baedal.dto.response.OrderNestedResponseDto;
import com.example.baedal.dto.response.OrderResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.ItemRepository;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.OrderHasItemRepository;
import com.example.baedal.repository.OrderRepository.OrderRepository;
import com.querydsl.core.types.Order;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

import static java.util.stream.Collectors.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    private final OrderHasItemRepository orderHasItemRepository;
    private final MemberService memberService;

    @Transactional
    public ResponseDto<?> postOrder(OrderRequestDto requestDto) {
        if (requestDto.getItemId().size() == 0) {
            return ResponseDto.fail("NEED_OVER_ONE","음식을 하나이상 주문해야합니다.");
        }
        MemberResponseDto member = memberService.isPresentMember(requestDto.getMemberId());
        if(null == member) {
            return ResponseDto.fail("NOT_FOUND", "memberID is not exist");
        }


        //itemId에 해당하는 내용들을 찾아서 리스트로
        List<Item> itemList = requestDto.getItemId()
                .stream()
                .map(item -> itemRepository.findByItemId(item).orElse(null))
                .collect(toList());
        //item을 OrderHasItems에 넣어두기
        Orders order = Orders.builder()
                .member(memberRepository.findByMemberId(requestDto.getMemberId()).orElse(null))
                .store(storeRepository.findByStoreId(requestDto.getStoreId()).orElse(null))
                .build();
        //System.out.println("storeName 잘 나오나 보자" + storeRepository.getStoreName(requestDto.getStoreId()));

        orderRepository.save(order);

//        List<OrderHasItem> orderHasItems = itemList.stream().map(item -> OrderHasItem.builder()
//                .orders(order)
//                .item(item)
//                .amount(requestDto.getAmount())
//                .build()).collect(Collectors.toList());

        //orderHasItemRepository.saveAll(orderHasItems);
        //System.out.println(requestDto.getAmount());
    for (int i=0; i<requestDto.getItemId().size(); i++){
        Item item = itemList.get(i);
        Integer amount = requestDto.getAmount().get(i);
        OrderHasItem item1 = OrderHasItem.builder()
                .orders(order)
                .item(item)
                .amount(amount)
                .build();
        if((item.getAmount() - amount)<0) {
            return ResponseDto.fail("NOT_ENOUGH_ITEM","재고가 부족합니다");
        }
        orderHasItemRepository.save(item1);
        item.setAmount(item.getAmount()-amount);
    }



        return ResponseDto.success(
                OrderResponseDto.builder()
                        .amount(requestDto.getAmount())
                        .itemId(requestDto.getItemId())
                        .memberId(requestDto.getMemberId())
                        .storeId(requestDto.getStoreId())
                        .createdAt(order.getCreatedAt())
                        //.modifiedAt(order.getModifiedAt())
                        .build());

    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllOrder() {
        //memberId, storeId, amount, item
        //return ResponseDto.success(orderHasItemRepository.findAll().stream().map(v->v.getOrders()));
        //return ResponseDto.success(orderHasItemRepository.findAll());

        //before refactoring
//        List<AllOrderResponseDto> collect = orderHasItemRepository.findAll().stream().map(v -> AllOrderResponseDto.builder()
//                .ordersId(v.getOrders().getOrdersId())
//                .itemId(v.getItem().getItemId())
//                .memberId(v.getOrders().getMember().getMemberId())
//                .storeId(v.getItem().getStore().getStoreId()).build()).collect(Collectors.toList());

//        return ResponseDto.success(collect);
//        return ResponseDto.success(orderHasItemRepository.findAll());

        //===================================================================
        //after refactoring
        //memberId(memberName?), storeId(storeName?), itemId(name?), itemAmount, itemPrice, createdAt

        //comparison1) Repository findAll
        //List<Orders> orders = orderRepository.findAll();

        //comparison2)
        List<Orders> orders = orderRepository.getAllOrder();
        List<OrderNestedResponseDto> collect = orders.stream()
                .map(OrderNestedResponseDto::new)
                .collect(toList());

        return ResponseDto.success(collect);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getOneOrder(Long id) {

        //comparison1) JPA 사용
        //return ResponseDto.success(orderRepository.getOneOrder(id));

        //comparison2)
        Orders orders = orderRepository.getOneOrder(id);
        OrderNestedResponseDto collectOne =
                new OrderNestedResponseDto(orders);

        return ResponseDto.success(collectOne);
    }

}
