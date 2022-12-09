package com.example.baedal.service;

import com.example.baedal.domain.Item;
import com.example.baedal.domain.Member;
import com.example.baedal.domain.Store;
import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.PessimisticLockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Transactional
@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class) //가짜 객체 명시
public class OrderServiceTest extends ServiceCustom {

    /*DB lock 없을 시, RunTime exception 터짐.
     * 1) PessimisticLockingFailure Exception
     * Pessimistic locking assumes that concurrent transactions will conflict with each other,
     * and requires resources to be locked after they are read and only unlocked after the application has finished using the data
     * 2) LockTimeOutException
     * obtainig a lock or converting a 'shared' lock(Pessimistic Read?) to 'exclusive'(Pessimistic Write?)*/
    @Test
    @DisplayName("주문 요청 동시에 진행할 시 재고 정상적으로 바뀌는지 확인")
    public void 주문_요청_동시성_테스트() {
        //given
        Member member1 = createMember("member1", "member1", "1234", "address1", "CONSUMER");
        memberRepository.save(member1);
        Member member2 = createMember("member2","member2","12345","address2","CONSUMER");
        memberRepository.save(member2);
        Store store = createStore( "store1","address1","category1",member1.getMemberId());
        storeRepository.save(store);
        Item item = createItem( "item1", 1000, 8000,"category1",store.getStoreId());
        itemRepository.save(item);
        log.info("member1, member2, store1, item1 생성 완료");

        //when(memberService.isPresentMember(any())).thenReturn(member1);

        OrderRequestDto order1 = createOrderRequestDto(store.getStoreId(), item.getItemId(), 1, member1.getMemberId());
        log.info(member1.getMemberId() + "의 주문 요청 생성 완료");
        log.info(order1.getMemberId() + "의 주문 요청 생성 완료");

        OrderRequestDto order2 = createOrderRequestDto(store.getStoreId(), item.getItemId(), 2, member2.getMemberId());
        log.info(member2.getMemberId() + "의 주문 요청 생성 완료");
        log.info(order2.getMemberId() + "의 주문 요청 생성 완료");

        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);    //concurrent : 2
        CountDownLatch latch = new CountDownLatch(threadCount);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + "1234567890");
        request.addHeader("Refresh-Token", "1234567890");

        //when
        try {
            executorService.execute(() -> {
                orderService.postOrder(order1, request);
                latch.countDown();
            });

            executorService.execute(() -> {
                orderService.postOrder(order2, request);
                latch.countDown();
            });
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //then
        int amount = itemRepository.findAmountByItemId(item.getItemId());
        assertThat(amount).isEqualTo(997);

    }
    }

