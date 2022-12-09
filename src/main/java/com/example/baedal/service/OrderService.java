package com.example.baedal.service;

import com.example.baedal.config.aop.LogExecutionTime;
import com.example.baedal.domain.Item;
import com.example.baedal.domain.OrderHasItem;
import com.example.baedal.domain.Orders;
import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.OrderNestedResponseDto;
import com.example.baedal.dto.response.OrderResponseDto;
import com.example.baedal.dto.response.ResponseDto;
import com.example.baedal.error.CustomException;
import com.example.baedal.error.ErrorCode;
import com.example.baedal.jwt.TokenProvider;
import com.example.baedal.repository.ItemRepository.ItemRepository;
import com.example.baedal.repository.MemberRepository.MemberRepository;
import com.example.baedal.repository.OrderHasItemRepository;
import com.example.baedal.repository.OrderRepository.OrderRepository;
import com.example.baedal.repository.StoreRepository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    private final OrderHasItemRepository orderHasItemRepository;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    //=====================================v1) select for update(select 할 때부터 Lock)============================================
    /*@Transactional annotation은 기본적으로 Checked Exception에 대해
    rollback 시키지 않도록 설계되어 있음. -> @Transactional(rollbackFor = Exception.class)로 해결.
    동시성 문제 해결 시작*/
    @LogExecutionTime
    @Transactional(rollbackFor = {CustomException.class})
    public ResponseDto<?> postOrder(OrderRequestDto requestDto, HttpServletRequest request) {

        //case1)case2)token validity check
        tokenProvider.tokenValidationCheck(request);

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
        requestDto.getItemId().forEach(itemId -> {
            if (!itemRepository.existsById(itemId)) {
                throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
            }
        });

        //case6)Item 주문 수량이 0일 때
        if (requestDto.getItemId().size() == 0) {
            throw new CustomException(ErrorCode.NEED_OVER_ONE);
        }

        //itemId에 해당하는 내용들을 찾아서 리스트로
        long start1 = System.currentTimeMillis();
        List<Item> itemList = requestDto.getItemId()
                .stream()
                .map(item -> itemRepository.findByItemId(item).orElse(null))    //x-Lock(pessimistic_write)
                .collect(toList());
        log.info("itemRepo 조회 후 List로 만드는데 걸리는 시간: " + (System.currentTimeMillis() - start1) + "ms");

        //item을 OrderHasItems에 넣어두기
        long start2 = System.currentTimeMillis();
        Orders order = Orders.builder()
                .member(memberRepository.findByMemberId(requestDto.getMemberId()).orElse(null)) //s-Lock
                .store(storeRepository.findByStoreId(requestDto.getStoreId()).orElse(null)) //s-Lock
                .build();
        log.info("memberRepo, storeRepo 조회 후 Orders로 만드는데 걸리는 시간: " + (System.currentTimeMillis() - start2) + "ms");

        long start3 = System.currentTimeMillis();
        orderRepository.save(order);    //s-Lock
        log.info("orderRepo에 저장하는 시간: " + (System.currentTimeMillis() - start3) + "ms");

//        List<OrderHasItem> orderHasItems = itemList.stream().map(item -> OrderHasItem.builder()
//                .orders(order)
//                .item(item)
//                .amount(requestDto.getAmount())
//                .build()).collect(Collectors.toList());

        //orderHasItemRepository.saveAll(orderHasItems);
        //System.out.println(requestDto.getAmount());
    long start4 = System.currentTimeMillis();
    for (int i=0; i<requestDto.getItemId().size(); i++){
        Item item = itemList.get(i);
        Integer amount = requestDto.getAmount().get(i);
        OrderHasItem item1 = OrderHasItem.builder()
                .orders(order)
                .item(item)
                .amount(amount)
                .build();
        orderHasItemRepository.save(item1); //s-Lock

        //stock update
        //System.out.println("previous amount :" + item.getAmount());
        //Integer stock = item.changeStock(amount);
        item.changeStock(amount);
        //System.out.println("stock : " + stock);
        log.info("OrderHasItemRepo에 저장 후 재고 변경까지 하는데 걸리는 시간: " + (System.currentTimeMillis() - start4) + "ms");

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

    //==========================================v2) select 할때는 lock 걸어두지 않음 ========================================
//    @LogExecutionTime
//    @Transactional(rollbackFor = {CustomException.class})
//    public ResponseDto<?> postOrderWithPessimisticRead(OrderRequestDto requestDto, HttpServletRequest request) {
//
//        //case1)case2)token validity check
//        tokenProvider.tokenValidationCheck(request);
//
//        //case3)
//        //MemberResponseDto member = memberService.isPresentMember(requestDto.getMemberId());
//        memberService.isPresentMember(requestDto.getMemberId());
////                if(null == member) {
////            return ResponseDto.fail("NOT_FOUND", "memberID is not exist");
////        }
//
//        //case4)StoreId에 해당하는 가게가 존재하지 않을 때
//        if (!storeRepository.existsById(requestDto.getStoreId())) {
//            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
//        }
//
//        //case5)ItemId에 해당하는 Item이 존재하지 않을 때
//        requestDto.getItemId().forEach(itemId -> {
//            if (!itemRepository.existsById(itemId)) {
//                throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
//            }
//        });
//
//        //case6)Item 주문 수량이 0일 때
//        if (requestDto.getItemId().size() == 0) {
//            throw new CustomException(ErrorCode.NEED_OVER_ONE);
//        }
//
//        //itemId에 해당하는 내용들을 찾아서 리스트로
//        long start1 = System.currentTimeMillis();
//        List<Item> itemList = requestDto.getItemId()
//                .stream()
//                .map(item -> itemRepository.findByItemId(item).orElse(null))    //s-Lock(pessimistic_write)
//                .collect(toList());
//        log.info("itemRepo 조회 후 List로 만드는데 걸리는 시간: " + (System.currentTimeMillis() - start1) + "ms");
//
//        //item을 OrderHasItems에 넣어두기
//        long start2 = System.currentTimeMillis();
//        Orders order = Orders.builder()
//                .member(memberRepository.findByMemberId(requestDto.getMemberId()).orElse(null)) //s-Lock
//                .store(storeRepository.findByStoreId(requestDto.getStoreId()).orElse(null)) //s-Lock
//                .build();
//        log.info("memberRepo, storeRepo 조회 후 Orders로 만드는데 걸리는 시간: " + (System.currentTimeMillis() - start2) + "ms");
//
//        long start3 = System.currentTimeMillis();
//        orderRepository.save(order);    //s-Lock
//        log.info("orderRepo에 저장하는 시간: " + (System.currentTimeMillis() - start3) + "ms");
//
//        long start4 = System.currentTimeMillis();
//        for (int i=0; i<requestDto.getItemId().size(); i++){
//            Item item = itemList.get(i);
//            Integer amount = requestDto.getAmount().get(i);
//            OrderHasItem item1 = OrderHasItem.builder()
//                    .orders(order)
//                    .item(item)
//                    .amount(amount)
//                    .build();
//            orderHasItemRepository.save(item1); //s-Lock
//
//            //stock 관련 exception
//            Integer amountBefore = itemRepository.findAmountByItemId(item.getItemId());
//            if (amountBefore < amount){
//                throw new CustomException(ErrorCode.NOT_ENOUGH_STOCK);
//            } else if(amountBefore.equals(amount)){
//                throw new CustomException(ErrorCode.OUT_OF_STOCK);
//            } else{
//                itemRepository.updateItemAmount(item.getItemId(),amount);   //stock update, pessimistic lock(x-lock)
//            }
//
//            log.info("OrderHasItemRepo에 저장 후, itemRepo에서 amount 찾고 exception, 재고 변경까지 하는데 걸리는 시간: " + (System.currentTimeMillis() - start4) + "ms");
//
//        }
//
//        return ResponseDto.success(
//                OrderResponseDto.builder()
//                        .amount(requestDto.getAmount())
//                        .itemId(requestDto.getItemId())
//                        .memberId(requestDto.getMemberId())
//                        .storeId(requestDto.getStoreId())
//                        .createdAt(order.getCreatedAt())
//                        //.modifiedAt(order.getModifiedAt())
//                        .build());
//
//    }
////=============================================================================================================


    @Transactional(readOnly = true)
    public ResponseDto<?> getAllOrder(HttpServletRequest request) {

        //case1)case2)token validity check
        tokenProvider.tokenValidationCheck(request);

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
    public ResponseDto<?> getAllOrderWithPaging(Pageable pageable, HttpServletRequest request) {

        //case1)case2)token validity check
        tokenProvider.tokenValidationCheck(request);

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
    public ResponseDto<?> getAllOrdersWithJPAPaging(Pageable pageable,HttpServletRequest request) {

        //case1)case2)token validity check
        tokenProvider.tokenValidationCheck(request);

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
    public ResponseDto<?> getOneOrder(Long id, HttpServletRequest request) {

        //case1)case2)token validity check
        tokenProvider.tokenValidationCheck(request);

        //comparison1) JPA 사용
        //return ResponseDto.success(orderRepository.getOneOrder(id));

        //comparison2)
        Orders orders = orderRepository.getOneOrder(id);
        OrderNestedResponseDto collectOne =
                new OrderNestedResponseDto(orders);

        return ResponseDto.success(collectOne);
    }


}
