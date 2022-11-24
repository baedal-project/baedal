package com.example.baedal.service;

import com.example.baedal.domain.Item;
import com.example.baedal.domain.OrderHasItem;
import com.example.baedal.domain.Orders;
import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.AllOrderResponseDto;
import com.example.baedal.dto.response.OrderResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.ItemRepository;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.OrderHasItemRepository;
import com.example.baedal.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    private final OrderHasItemRepository orderHasItemRepository;

    @Transactional
    public ResponseDto<?> postOrder(OrderRequestDto requestDto) {

        //itemId에 해당하는 내용들을 찾아서 리스트로
        List<Item> itemList = requestDto.getItemId()
                .stream()
                .map(item -> itemRepository.findByItemId(item).orElse(null))
                .collect(Collectors.toList());


        //item을 OrderHasItems에 넣어두기
        Orders order = Orders.builder()
                .member(memberRepository.findByMemberId(requestDto.getMemberId()).orElse(null))
                .build();
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
        orderHasItemRepository.save(item1);
    }

        return ResponseDto.success(
                OrderResponseDto.builder()
                        .amount(requestDto.getAmount())
                        .itemId(requestDto.getItemId())
                        .memberId(requestDto.getMemberId())
                        .storeId(requestDto.getStoreId())
                        .createdAt(order.getCreatedAt())
                        .modifiedAt(order.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> getAllOrder() {
        //memberId, storeId, amount, item
        //return ResponseDto.success(orderHasItemRepository.findAll().stream().map(v->v.getOrders()));
        //return ResponseDto.success(orderHasItemRepository.findAll());

        List<AllOrderResponseDto> collect = orderHasItemRepository.findAll().stream().map(v -> AllOrderResponseDto.builder()
                .ordersId(v.getOrders().getOrdersId())
                .itemId(v.getItem().getItemId())
                .memberId(v.getOrders().getMember().getMemberId())
                .storeId(v.getItem().getStore().getStoreId()).build()).collect(Collectors.toList());

        return ResponseDto.success(collect);
        //return ResponseDto.success(orderHasItemRepository.findAll());


    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getOneOrder(Long id) {
        return ResponseDto.success(orderRepository.findByOrdersId(id));
    }

}
