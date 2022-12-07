package com.example.baedal.service;

import com.example.baedal.domain.Item;
import com.example.baedal.domain.OrderHasItem;
import com.example.baedal.domain.Orders;
import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.OrderNestedResponseDto;
import com.example.baedal.dto.response.OrderResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.error.CustomException;
import com.example.baedal.error.ErrorCode;
import com.example.baedal.repository.ItemRepository.ItemRepository;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.OrderHasItemRepository;
import com.example.baedal.repository.OrderRepository.OrderRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.*;

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
    public ResponseDto<?> postOrder(OrderRequestDto requestDto, HttpServletRequest request) {

        //case1) Refresh-Token이 Null일 때
        if (null == request.getHeader("Refresh-Token")) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        //case2) Authorization이 Null일 때
        if (null == request.getHeader("Authorization")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        //case3)
        //MemberResponseDto member = memberService.isPresentMember(requestDto.getMemberId());
        memberService.isPresentMember(requestDto.getMemberId());
//                if(null == member) {
//            return ResponseDto.fail("NOT_FOUND", "memberID is not exist");
//        }

        //case4)StoreId에 해당하는 가게가 존재하지 않을 때
        if (!storeRepository.existsById(requestDto.getStoreId())) {
            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }

        //case5)ItemId에 해당하는 Item이 존재하지 않을 때
        requestDto.getItemId().stream().forEach(itemId -> {
            if (!itemRepository.existsById(itemId)) {
                throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
            }
        });

        //case6)Item 주문 수량이 별로 없을 때
        if (requestDto.getItemId().size() == 0) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }
        //case6)
        //MemberResponseDto member = memberService.isPresentMember(requestDto.getMemberId());
        memberService.isPresentMember(requestDto.getMemberId());


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
        orderHasItemRepository.save(item1);
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

        //==============================Refactor v1=====================================
        //memberId(memberName?), storeId(storeName?), itemId(name?), itemAmount, itemPrice, createdAt

        //comparison1) Repository findAll
        //List<Orders> orders = orderRepository.findAll();

        //==============================Refactor v2======================================
        List<Orders> orders = orderRepository.getAllOrder();
        List<OrderNestedResponseDto> collect = orders.stream()
                .map(OrderNestedResponseDto::new)
                .collect(toList());

        return ResponseDto.success(collect);

    }

    //==========================Refactor v3(solve paging problem)========================================
        /*problem :
        OrderNestedResponseDto안에 OrderItems
        -> FetchType.LAZY
        -> stream 돌 때 orderhasitems 수만큼 select query
            + orderhasitems안에 Item수만큼 select query*/
    @Transactional
    public ResponseDto<?> getAllOrderWithPaging(Pageable pageable){
        Page<Orders> orderWithPaging = orderRepository.getAllOrderWithPaging(pageable);
        List<OrderNestedResponseDto> collect = orderWithPaging.stream()
                //.map(OrderNestedResponseDto::new)
                .map(v -> new OrderNestedResponseDto(v))
                .collect(toList());
        List<Object> count = new ArrayList<>();
        count.add(collect);
        HashMap<String,Integer> counts = new HashMap<>();
        counts.put("pages",orderWithPaging.getTotalPages());
        count.add(counts);
        //return ResponseDto.success(collect);
        return ResponseDto.success(count);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllOrdersWithJPAPaging(Pageable pageable) {
        Page<Orders> page = orderRepository.findAll(pageable);
        List<OrderNestedResponseDto> collect = page.stream()
                //.map(OrderNestedResponseDto::new)
                .map(OrderNestedResponseDto::new)
                .collect(toList());
        List<Object> count = new ArrayList<>();
        count.add(collect);
        HashMap<String,Integer> counts = new HashMap<>();
        counts.put("pages",page.getTotalPages());
        count.add(counts);
        return ResponseDto.success(count);
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
