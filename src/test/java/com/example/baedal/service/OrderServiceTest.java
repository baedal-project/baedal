package com.example.baedal.service;

import com.example.baedal.domain.Item;
import com.example.baedal.dto.request.OrderRequestDto;
import com.example.baedal.dto.response.ResponseDto;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.PessimisticLockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;


@Transactional
@SpringBootTest
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
        //init stock of itemId 5L : 8
        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        OrderRequestDto order1 = createOrderRequestDto(3L, 5L, 1, 1L);
        OrderRequestDto order2 = createOrderRequestDto(3L, 5L, 2, 2L);

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
            //then
            SoftAssertions softAssertions = new SoftAssertions();
            softAssertions.assertThat(e).isInstanceOf(InterruptedException.class);
            softAssertions.assertThat(e).isInstanceOf(PessimisticLockException.class);
            softAssertions.assertThat(e).isInstanceOf(LockTimeoutException.class);
        }
    }
}
