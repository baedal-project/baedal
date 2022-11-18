package com.example.baedal.service;

import com.example.baedal.domain.Item;
import com.example.baedal.domain.OrderHasItem;
import com.example.baedal.domain.Orders;
import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.repository.ItemRepository;
import com.example.baedal.repository.MemberRepository;
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

    @Transactional
    public ResponseDto<?> postOrder(OrderRequestDto requestDto) {

        //itemId에 해당하는 내용들을 찾아서 리스트로
        List<Item> itemList = requestDto.getItemId()
                .stream()
                .map(item -> itemRepository.findById(item).orElse(null))
                .collect(Collectors.toList());


        //item을 OrderHasItems에 넣어두기
        Orders order = Orders.builder()
                .orderHasItems(itemList.stream()
                        .map(OrderHasItem::new)
                        .collect(Collectors.toList()))
                .member(memberRepository.findById(requestDto.getMemberId()).orElse(null))
                .build();
        orderRepository.save(order);

        return ResponseDto.success(requestDto.getItemId());
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllOrder() {
        return ResponseDto.success(orderRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getOneOrder(Long id) {
        return ResponseDto.success(orderRepository.findById(id));
    }

}
